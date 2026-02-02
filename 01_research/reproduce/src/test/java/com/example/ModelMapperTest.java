package com.example;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelMapperTest {

    public static class Source {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    public static class Destination {
        private String teSt;

        public String getTeSt() {
            return teSt;
        }

        public void setTeSt(String teSt) {
            this.teSt = teSt;
        }
    }

    @Test
    public void testCaseSensitivity() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Uncomment to test configuration
        // modelMapper.getConfiguration().setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.LOOSE);
        
        Source source = new Source();
        source.setTest("Hello World");
        
        Destination dest = modelMapper.map(source, Destination.class);
        
        assertEquals("Hello World", dest.getTeSt(), "Mapping should work despite case difference");
    }
}
