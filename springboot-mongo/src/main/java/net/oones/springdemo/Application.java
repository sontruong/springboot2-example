package net.oones.springdemo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.oones.springdemo.domain.Lang;
import net.oones.springdemo.domain.LangItem;
import net.oones.springdemo.domain.Project;
import net.oones.springdemo.repository.LangRepository;
import net.oones.springdemo.repository.ProjectRepository;

/**
 * @author Son.Truong
 *
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private LangRepository langRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		projectRepository.deleteAll();
		langRepository.deleteAll();
		// save a couple of customers
		projectRepository.save(new Project("OOne Language", "Project"));
		projectRepository.save(new Project("OOne Organization", "Project"));

		// fetch all customers
		System.out.println("Project found with findAll():");
		System.out.println("-------------------------------");
		for (Project obj : projectRepository.findAll()) {
			System.out.println(obj);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('OOne Organization'):");
		System.out.println("--------------------------------");
		Project project = projectRepository.findTop1ByName("OOne Organization");
		System.out.println(project);
		
		Lang lang = new Lang();
		lang.key = "prj.dashboard";
		lang.projectId = project.id;
		Collection<LangItem> items = new ArrayList<LangItem>();
		items.add(new LangItem("vn", "Bảng điều khiển"));
		items.add(new LangItem("en", "Dashboard"));
		lang.values = items;
		
		langRepository.save(lang);
	}

}
