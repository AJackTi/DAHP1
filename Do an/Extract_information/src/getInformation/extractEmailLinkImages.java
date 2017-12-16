package getInformation;

/*
TEN SV: DANG VO DUC TRONG	MSSV: 15055721
TEN SV: NGUYEN CHI THANG	MSSV: 15055101
GIANG VIEN HUONG DAN: HUYNH THAI HOC
link github: https://github.com/AjackTi/DAHP1
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class extractEmailLinkImages {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
				String url = null ;
				String content = fetchHtml(url);
						
				// email
				System.out.println(filterEmail(content));
				
				// image
				ArrayList<String> arr_img = filterImages(content);
				
				// link
				ArrayList<String> arr_link = filterLink(content);
				
	}
	public static String filterEmail(String content)
	{
		String pattern = "([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(content);
		String str_email = "";
		while(m.find())
		{
			str_email += m.group(0) + "\n";
		}
		return str_email;
	}
	
	public static String fetchHtml(String url_input)
	{
		// Get HTML
				String content = "";
				try {
		            // get URL content
		            String urlStr = url_input;
		            URL url = new URL(urlStr);
		            URLConnection connection = url.openConnection();

		            InputStream inputStream = connection.getInputStream();
		            
		            // open the stream and put it into BufferedReader
		            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		            String line = br.readLine();
					
					while(line !=null) {
						content += line;
						line = br.readLine();
					}
		            br.close();
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				return content;
	}
	
	public static ArrayList<String> filterImages(String content) throws IOException
	{
		// Lấy các phần tử của trang giống như thẻ img(<img src="" alt="">)
				// Ở đây hơi rắc rối nên nhóm quyết định tách ra làm 2 pattern:
				// Pattern 1: Lấy ra thẻ <img > từ trong page source ra
				// Pattern 2: Lấy link(chứa ảnh ra)
				String pattern3 = "<img src=\"(.+?)\"";
				Pattern p3 = Pattern.compile(pattern3);
				Matcher m3 = p3.matcher(content);
				ArrayList<String> arr_list1 = new ArrayList<>();
				while(m3.find())
				{
					arr_list1.add(m3.group(0));
				}
				
				ArrayList<String> arr_list1_1 = new ArrayList<>();
				for (String string : arr_list1) {
					//String pattern3_1 = "\"(.+?)\"";
					String pattern3_1 = "https(.+?){1,}[^\"]";
					Pattern p3_1 = Pattern.compile(pattern3_1);
					Matcher m3_1 = p3_1.matcher(string);
					while(m3_1.find())
					{
						arr_list1_1.add(m3_1.group(0) + "\n");
					}
				}
				
				// SAVE IMAGES
				// Ở đây ta có được 1 list danh sách các image trong page source 
				// lấy current folder
				final String dir = System.getProperty("user.dir");
				// tạo 1 thư mục
				new File("Images").mkdir();
//				System.setProperty("user.dir", dir + "\\Images");
				int count = 0;
				for (String string : arr_list1_1) {
					String destinationFile = "image" + count +".jpg";
					count ++;
					saveImage(string, destinationFile);
				}
				return arr_list1_1;
	}
	
	public static ArrayList<String> filterLink(String content)
	{
		// dựa vào thẻ <a href="">
		String pattern = "href=\"(.+?)\"";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(content);
		ArrayList<String> arr_list_href = new ArrayList<>();
		while(m.find())
		{
			arr_list_href.add(m.group(0));
		}
		ArrayList<String> arr_result = new ArrayList<>();
		for (String string : arr_list_href) {
			pattern = "\"(.+?)\"";
			p = Pattern.compile(pattern);
			m = p.matcher(string);
			while(m.find())
			{
				String str_re = m.group(0);
				str_re = str_re.replace("\"", "");
				if(str_re.toLowerCase().contains("#") || str_re.toLowerCase().contains("javascript".toLowerCase()) || str_re.toLowerCase().contains("style=") || str_re.toLowerCase().contains("><img src=") || str_re.equals("//") || str_re.equals("/") )
				{
					continue;
				}
				else
				{
					arr_result.add(str_re + "\n");
				}
			}
		}
		return arr_result;
	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		try(InputStream in = new URL(imageUrl).openStream()){
		    Files.copy(in, Paths.get("./Images/"+ destinationFile));
		}
		catch (Exception e) {
			System.out.println("Error");
		}
	}

}
