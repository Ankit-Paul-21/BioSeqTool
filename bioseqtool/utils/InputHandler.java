package utils;

import java.util.*;

public class InputHandler 
{
    public static String conjoinEntry(String str) 
    {
    	if (str == null || str.trim().isEmpty())
    	{
    		return "";
    	}
    	
        str = str.trim();
        String[] words = str.split("\\s+");
        
        StringBuilder res = new StringBuilder();
        
        for (String word : words)
        {
        	res.append(word);
        }
        
        return res.toString();    
    }
}