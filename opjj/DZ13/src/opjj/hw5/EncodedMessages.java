package opjj.hw5;

public final class EncodedMessages {

    public static final String MESSAGE_1 = "'Purjudpplqj lv olnh vha: rqh plvwdnh dqg brx duh surylglqj vxssruw iru d olihwlph.' -- Mlfkdho Slqc";
    public static final String MESSAGE_2 = "'Aalpnh rdst ph xu iwt vjn lwd tcsh je bpxcipxcxcv ndjg rdst xh kxdatci ehnrwdepiw lwd zcdlh lwtgt ndj axkt.' -- Mpgixc Gdasxcv";

    public static void main(String[] args) {
        CaesarCipher cipher = new CaesarCipher();

        String m1 = null;
        String m2 = null;
        
        for(int i = 0; i < 26; i++) {
        	m1 = cipher.decode(MESSAGE_1, i);
        	System.out.println("Shift = " + i + ": " + m1);
        }	// discovered: shift 3 to decode
        
        for(int i = 0; i < 26; i++) {
        	m2 = cipher.decode(MESSAGE_2, i);
        	System.out.println("Shift = " + i + ": " + m2);
        }	// discovered: shift 15 to decode
    }

}
