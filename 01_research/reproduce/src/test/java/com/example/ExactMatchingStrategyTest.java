package com.example;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MatchingStrategy;
import org.modelmapper.spi.MatchingStrategy.MatchingStrategies; // Interface conflict fix might be needed, checking import
// Correct import usually: import org.modelmapper.spi.MatchingStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExactMatchingStrategyTest {

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
    public void testCustomExactMatchingStrategy() {
        ModelMapper modelMapper = new ModelMapper();

        // Define Custom Matching Strategy
        // Note: Full implementation usually requires implementing the 'matches' method
        // which takes context.
        // Since we can't easily implement a full Strategy from scratch without diving
        // deep into
        // internal SPIs (tokens etc), we will try to Configure the Configuration
        // to use a specific behavior or verify if STRICT can be tweaked.

        // RE-EVALUATION: Implementing a full MatchingStrategy interface is complex.
        // Let's try to simulate what "Exact Match" means.
        // Actually, if STRICT fails, it means STRICT is NOT exact enough for tokens.

        // Let's try STRICT again but with a different Tokenizer?
        // If we use limit Introspector or similar?

        // Alternative: Use a Condition?

        // For this test, let's try configuring the SourceNameTokenizer to be Literal?
        // If "xType" is tokenized as ["xType"] instead of ["x", "Type"],
        // and "xTYPE" as ["xTYPE"], then STRICT should work perfectly.

        modelMapper.getConfiguration().setSourceNameTokenizer((name, type) -> new String[] { name });
        modelMapper.getConfiguration().setDestinationNameTokenizer((name, type) -> new String[] { name });

        // Set context to STRICT to ensure 1-to-1 match of those literal tokens
        modelMapper.getConfiguration().setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);

        Source source = new Source();
        source.setxType("Value-xType");
        source.setxTYPE("Value-xTYPE");

        Destination dest = modelMapper.map(source, Destination.class);

        System.out.println("dest.getxType() = " + dest.getxType());
        System.out.println("dest.getxTYPE() = " + dest.getxTYPE());

        assertEquals("Value-xType", dest.getxType());
        assertEquals("Value-xTYPE", dest.getxTYPE());
    }
}
