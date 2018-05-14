
import java.io.*;
import java.security.*;

public class ComputeSHA{
    
    public static void main(String []args) throws NoSuchAlgorithmException, IOException {
        System.out.println(sha1(args[0]));
    }
    
    
    static String sha1(String file) throws NoSuchAlgorithmException,IOException  {
        
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(file);
        
        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fis.read(data)) != -1 ){
            sha1.update(data,0, read);
        }
        byte[] hashBytes = sha1.digest();
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
    }
}

  
