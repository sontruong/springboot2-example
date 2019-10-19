/**
 * 
 */
package net.oones.springdemo.domain;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;

/**
 * @author Son.Truong
 *
 */
public class Project {
	@Id
    public BigInteger id;

    public String name;
    public String apiKey;

    public Project() {}

    public Project(String name, String apiKey) {
        this.name = name;
        this.apiKey = apiKey;
    }
    
    
}
