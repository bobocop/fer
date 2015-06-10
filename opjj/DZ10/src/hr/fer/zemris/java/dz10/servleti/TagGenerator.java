package hr.fer.zemris.java.dz10.servleti;

public class TagGenerator {
	
	public static String generateBodyBGColor(String color) {
		String tag = "<body bgcolor=\"";
		if(color == null) {
			tag += "white";
		} else {
			tag += color;
		}
		tag += "\">";
		return tag;
	}
}
