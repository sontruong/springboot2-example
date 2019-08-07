package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.demo.domain.GPX;
import com.example.demo.domain.GpxMetadata;
import com.example.demo.domain.Track;
import com.example.demo.domain.TrackPoint;
import com.example.demo.domain.TrackSeg;
import com.example.demo.domain.Waypoint;
import com.example.demo.exception.ContentException;
import com.example.demo.exception.NotfoundException;
import com.example.demo.repository.GPSRepository;
import com.example.demo.repository.TrackRepository;
import com.example.demo.utils.AppConfig;
import com.example.demo.utils.AppUtils;
import com.example.demo.utils.ApplicationMessage;

@Service
public class GPSServiceImpl implements GPSService {

	private static Logger LOGGER = LoggerFactory.getLogger(GPSServiceImpl.class);
	@Autowired
	GPSRepository gpsRepository;
	@Autowired
	TrackRepository trackRepository;

	@Override
	public void importGPSTracker(MultipartFile multipartFile)
			throws IOException, ParserConfigurationException, SAXException {
		if (null == multipartFile || null == multipartFile.getBytes()) {
			throw new ContentException(ApplicationMessage.UPLOAD_FILE_REQUIRE);
		}
		GPX gpx = new GPX();
		gpx.setFileData(multipartFile.getBytes());
		gpx.setFilename(multipartFile.getOriginalFilename());

		File file = File.createTempFile("file" + new Date().getTime(), "gpx");
		FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
		Element element = AppUtils.readXML(file);
		GpxMetadata gpxMetadata = extractMetadata(element);
		gpxMetadata.setGpx(gpx);
		gpx.setMetadata(gpxMetadata);

		Collection<Waypoint> waypoints = extractWaypoints(element);
		gpx.setWpt(waypoints);

		gpsRepository.save(gpx);
		LOGGER.info("----" + gpx.getId() + " - " + gpx.getWpt().size());

		// because we may have alot of track, so I will separate them to new entity
		Collection<Track> tracks = extractTracks(element, gpx);
		trackRepository.saveAll(tracks);
	}

	private Collection<Track> extractTracks(Element element, GPX gpx) {
		Collection<Track> objs = new ArrayList<>();

		NodeList list = AppUtils.getNodes(element, "trk");
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			Track track = extractTrack(node);
			track.setGpxId(gpx.getId());
			objs.add(track);
		}
		return objs;
	}

	private Track extractTrack(Node node) {
		Track track = new Track();
		// name
		String name = AppUtils.getChildNodeValue(node, "name");
		track.setName(name);

		// trackseg
		NodeList list = AppUtils.getNodes(node, "trkseg");
		Collection<TrackSeg> trackSegs = extractSegs(list, track);
		track.setTrkseg(trackSegs);
		return track;
	}

	private Collection<TrackSeg> extractSegs(NodeList list, Track track) {
		Collection<TrackSeg> segs = new ArrayList<>();

		for (int i = 0; i < list.getLength(); i++) {
			TrackSeg trackSeg = new TrackSeg();
			trackSeg.setTrack(track);

			Collection<TrackPoint> trkpt = new ArrayList<>();
			Node node = list.item(i);
			NodeList lstTPNode = AppUtils.getNodes(node, "trkpt");
			for (int j = 0; j < lstTPNode.getLength(); j++) {
				TrackPoint trackPoint = new TrackPoint();
				Node node2 = lstTPNode.item(j);
				String lat = AppUtils.getNodeAttr(node2, "lat");
				String lon = AppUtils.getNodeAttr(node2, "lon");
				String ele = AppUtils.getChildNodeValue(node2, "ele");
				Double dLat = Double.valueOf(lat);
				Double dLon = Double.valueOf(lon);
				Double dEle = Double.valueOf(ele);
				String time = AppUtils.getChildNodeValue(node2, "time");
				trackPoint.setEle(dEle);
				trackPoint.setLat(dLat);
				trackPoint.setLon(dLon);
				try {
					trackPoint.setTime(AppUtils.parseDate(time));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				trkpt.add(trackPoint);
			}
			trackSeg.setTrkpt(trkpt);
			segs.add(trackSeg);
		}
		return segs;
	}

	private Collection<Waypoint> extractWaypoints(Element element) {
		Collection<Waypoint> waypoints = new ArrayList<>();
		NodeList list = AppUtils.getNodes(element, "wpt");
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			Waypoint waypoint = extractWaypoint(node);
			waypoints.add(waypoint);
		}
		return waypoints;
	}

	private Waypoint extractWaypoint(Node node) {
		String lat = AppUtils.getNodeAttr(node, "lat");
		String lon = AppUtils.getNodeAttr(node, "lon");
		Double dLat = Double.valueOf(lat);
		Double dLon = Double.valueOf(lon);
		String name = AppUtils.getChildNodeValue(node, "name");
		String sym = AppUtils.getChildNodeValue(node, "sym");

		Waypoint waypoint = new Waypoint();
		waypoint.setLat(dLat);
		waypoint.setLon(dLon);
		waypoint.setName(name);
		waypoint.setSym(sym);
		return waypoint;
	}

	private GpxMetadata extractMetadata(Element element) {
		GpxMetadata gpxMetadata = new GpxMetadata();

		Node meta = AppUtils.getNode(element, "metadata");
		String name = AppUtils.getChildNodeValue(meta, "name");
		String desc = AppUtils.getChildNodeValue(meta, "desc");
		String author = AppUtils.getChildNodeValue(meta, "author");
		String time = AppUtils.getChildNodeValue(meta, "time");

		Node link = AppUtils.getChildNode(meta, "link");
		String href = AppUtils.getNodeAttr(link, "href");
		String text = AppUtils.getChildNodeValue(link, "text");

		gpxMetadata.setAuthor(author);
		gpxMetadata.setDesc(desc);
		gpxMetadata.setName(name);
		gpxMetadata.setLinkHref(href);
		gpxMetadata.setLinkText(text);
		try {
			gpxMetadata.setTime(AppUtils.parseDate(time));
		} catch (Exception e) {
		}
		return gpxMetadata;
	}

	@Override
	public Collection<GPX> getLastest() {
		Sort sort = AppUtils.createSort(Direction.DESC, "metadata.time");
		PageRequest pageRequest = AppUtils.createPageRequest(0, AppConfig.PAGE_LIMIT, sort);
		Page<GPX> page = gpsRepository.findAll(pageRequest);
		return page.getContent();
	}

	@Override
	public GPX getDetail(Long id) {
		GPX gpx = gpsRepository.findById(id).orElse(null);
		if (null == gpx) {
			throw new NotfoundException(ApplicationMessage.GPX_NOT_FOUND);
		}
		Collection<Track> tracks = trackRepository.findByGpxId(gpx.getId());
		gpx.setTracks(tracks);
		return gpx;
	}

	@Override
	public void download(Long id, HttpServletResponse response) throws IOException {
		GPX gpx = gpsRepository.findById(id).orElse(null);
		if (null == gpx) {
			throw new NotfoundException(ApplicationMessage.GPX_NOT_FOUND);
		}
		
		InputStream bis = null;
		try {
			System.out.println(gpx.getFileData());
			response.setHeader("Content-disposition", "attachment; filename=" + gpx.getFilename());
			response.getOutputStream().write(gpx.getFileData());
			response.getOutputStream().flush();
		} catch (Exception e) {
			throw e;
		}
		finally {
			if(null != bis) {
				bis.close();
			}
		}
	}

}
