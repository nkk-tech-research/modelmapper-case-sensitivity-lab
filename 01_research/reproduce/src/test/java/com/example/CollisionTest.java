package com.example;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollisionTest {

    public static class Source {
        private String xType;
        private String xTYPE;

        public String getxType() {
            return xType;
        }

        public void setxType(String xType) {
            this.xType = xType;
        }

        public String getxTYPE() {
            return xTYPE;
        }

        public void setxTYPE(String xTYPE) {
            this.xTYPE = xTYPE;
        }
    }

    public static class Destination {
        private String xType;
        private String xTYPE;

        public String getxType() {
            return xType;
        }

        public void setxType(String xType) {
            this.xType = xType;
        }

        public String getxTYPE() {
            return xTYPE;
        }

        public void setxTYPE(String xTYPE) {
            this.xTYPE = xTYPE;
        }
    }

    @Test
    public void testCollisionStrict() {
        ModelMapper modelMapper = new ModelMapper();
        // Use STRICT strategy as per user report
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Source source = new Source();
        source.setxType("Value-xType");
        source.setxTYPE("Value-xTYPE");

        Destination dest = modelMapper.map(source, Destination.class);

        System.out.println("dest.getxType() = " + dest.getxType());
        System.out.println("dest.getxTYPE() = " + dest.getxTYPE());

        assertEquals("Value-xType", dest.getxType(), "xType should map to xType");
        assertEquals("Value-xTYPE", dest.getxTYPE(), "xTYPE should map to xTYPE");
    }
}
