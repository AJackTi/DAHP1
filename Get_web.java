package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Get_web {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            // get URL content

            String urlStr="http://kenh14.vn/hot-mama-2017-se-duoc-to-chuc-tai-viet-nam-vao-25-11-20170910231849133.chn";
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                    System.out.println(inputLine);
            }
            br.close();

            System.out.println("Done");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
