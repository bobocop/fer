package opjj.hw2;

import java.math.BigDecimal;

/**
 * Interprets the numbers from 0 to 999,999,999.99 as an amount in kunas and
 * converts it to text.
 */
public final class MoneyToTextConverter {
	private boolean millDecl = false;
	
    private static final String[] FROM_0_TO_20 = { "nula", "jedna", "dvije",
            "tri", "četiri", "pet", "šest", "sedam", "osam", "devet", "deset",
            "jedanaest", "dvanaest", "trinaest", "četrnaest", "petnaest",
            "šesnaest", "sedamnaest", "osamnaest", "devetnaest" };

    private static final String[] TENTHS = { "dvadeset", "trideset",
            "četrdeset", "pedeset", "šezdeset", "sedamdeset", "osamdeset",
            "devedeset" };
    
    private static final BigDecimal eps = BigDecimal.valueOf(0.005);

    private static final String[] genitiv = { "a", "a", "e", "e", "e", 
    		"a", "a", "a", "a", "a" };
    
    /**
     * Converts the money amount to text. <code>null</code> is not a valid
     * parameter.
     * @param money An amount to be converted.
     * @return A Croatian text which describes the amount.
     */
    public String convert(BigDecimal money) {
        if (money == null) {
            throw new IllegalArgumentException("No conversion for null");
        }

        BigDecimal lipe = money
				.subtract(BigDecimal.valueOf(money.intValue())).add(eps)
				.scaleByPowerOfTen(2);
        
        String ret = fromInteger(money.abs().intValue()) + " " + declKn(money)
        		+ " i " + fromInteger(lipe.intValue()) + " " + declLp(lipe);
        return ret.replace("jedna milijun", "jedan milijun");
    }

    private String fromInteger(int n) {
        if (n < 20) {
        	if(millDecl) {
        		millDecl = false;
        		if(n == 1) {
        			return "jedan";
        		} else if(n == 2) {
        			return "dva";
        		}
        	}
        	
            return FROM_0_TO_20[n];
        } else if (n < 100) {
            int div = n / 10;
            int mod = n % 10;
            int arrayStartsAt20Offset = 2;
            return TENTHS[div - arrayStartsAt20Offset] + continuationFor(mod);
        } else if (n < 1000) {
            return scale(n, 100, "stotin");
        } else if (n < 1000000){
            return scale(n, 1000, "tisuć");
        } else {
        	millDecl = true;
        	return scale(n, 1000000, "milijun");
        }
    }

    private String continuationFor(int n) {
        return n == 0 ? "" : " " + fromInteger(n);
    }

    private String scale(int n, int s, String text) {
        int div = n / s;
        int mod = n % s;
        String ret = fromInteger(div) + " " + decl(div, text) + continuationFor(mod);
        return ret;
    }
    
    private String declKn(BigDecimal money) {
    	int kn = money.abs().intValue();
    	if(kn % 100 >= 10 && kn <= 20) {
    		return "kuna";
    	}
    	return "kun" + genitiv[kn % 10];
    }
    
    private String declLp(BigDecimal lipe) {
    	int lp = lipe.abs().intValue();
    	if(lp >= 10 && lp <= 20) {
    		return "lipa";
    	}
    	return "lip" + genitiv[lp % 10];
    }

    private String decl(int n, String text) {
    	if(n >= 10 && n <= 20) {
    		return text + "a";
    	} else {
    		if(text.equals("milijun")) {
    			return (n % 10) == 1 ? "milijun" : "milijuna";
    		} else {
    			return text + genitiv[n % 10];
    		}
    	}
    }
}
