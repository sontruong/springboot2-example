package net.oones.springdemo.domain;

import java.math.BigInteger;
import java.util.Collection;

import org.springframework.data.annotation.Id;

public class Lang {
	@Id
    public BigInteger id;
	
    public BigInteger projectId;
    
    public String key;
    
    public Collection<LangItem> values;
}
