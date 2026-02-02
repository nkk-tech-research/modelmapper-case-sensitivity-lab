package com.example.poc.config;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.NameTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmartModelMapper {

    private static final Logger logger = LoggerFactory.getLogger(SmartModelMapper.class);

    private final ModelMapper primaryMapper;   // Standard (CamelCase separation)
    private final ModelMapper fallbackMapper;  // Strict (Literal separation)

    public SmartModelMapper() {
        // 1. Primary Mapper (Standard configuration)
        this.primaryMapper = new ModelMapper();
        // You can apply other standard configurations here if needed
        
        // 2. Fallback Mapper (Strict configuration for collisions)
        this.fallbackMapper = new ModelMapper();
        // Tokenizer that does not split tokens at all (Literal)
        NameTokenizer literalTokenizer = (name, nameableType) -> new String[]{name};
        
        this.fallbackMapper.getConfiguration()
            .setSourceNameTokenizer(literalTokenizer)
            .setDestinationNameTokenizer(literalTokenizer)
            .setMatchingStrategy(MatchingStrategies.STRICT); // Strict matching
    }

    /**
     * Hybrid mapping processing
     */
    public <D> D map(Object source, Class<D> destinationType) {
        try {
            // First attempt: Standard behavior
            return primaryMapper.map(source, destinationType);
            
        } catch (ConfigurationException | MappingException e) {
            // Only retry if it looks like an ambiguity/configuration issue that strict might solve.
            // For safety, we log and retry.
            logger.warn("Mapping failed with standard strategy. Retrying with strict/literal strategy. Cause: {}", e.getMessage());

            try {
                // Second attempt: Strict behavior for collision cases
                return fallbackMapper.map(source, destinationType);
                
            } catch (Exception fatalE) {
                // Both failed
                logger.error("Fallback mapping also failed.");
                throw fatalE;
            }
        }
    }
}
