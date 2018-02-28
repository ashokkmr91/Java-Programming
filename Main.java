import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.net.*;
import java.io.*;
public class DownloadManager {
	String targetDirectory=".";	
int i;
	public static void main(String args[]){
		Main obj=new Main();
		obj.interact();
	}
public void interact(){
	System.out.print("APP > Menu\n"+

"-------------------\n"+

"1. Download a file [ “url” as input ]\n"+

"2. Batch Download [ comma separated “urls” as input ]\n"+

"3. Settings [ configure download directory ]\n"+

"4. Exit\n");
	Scanner scanner=new Scanner(System.in);
	System.out.print("USER >");
	int input=0;
	try{
	 input=scanner.nextInt();
	}
	catch(Exception e){
		System.out.println("APP >Wrong Input ");
	}
	switch(input){
	case 1:{
		System.out.println("APP > Enter URL: ");
		System.out.print("USER >");
		String url=scanner.next();
		downloadFile(url);
		
		break;
	}
	case 2:{
		System.out.println("APP > Enter URLs: ");
		System.out.print("USER >");
		String urls=scanner.next();
		String []url=urls.split(",");
		downloadFiles(url);
		break;
	}
	case 3:{
		System.out.println("APP >Enter Destionation Dir");
		System.out.print("USER >");
		String des=scanner.next();
		if(isValidPath(des)){
			targetDirectory=des;
			System.out.println("APP >Destionation Dir Changed");
		}
		else
			System.out.println("APP >Invalid Destionation Dir");
		break;
	}
	case 4:{
		System.exit(0);break;
	}
	default:{
		System.out.println("APP >Wrong Input");
	}
	}
	interact();
}
private void downloadFiles(String[] urls) {
	
	Thread []threads =new Thread[urls.length];
	System.out.println(urls.length);
	for(i=0;i<urls.length;i++){
	String url=new String(urls[i]);
		threads[i]=new Thread(new Runnable() {
			
			@Override
			public void run() {
			downloadFile(url);
			}
		});
	}
	try {
		for (Thread thread : threads) {
		    thread.start();
		}
		for (Thread thread : threads) {
		    thread.join();
		}

	} catch (InterruptedException e) {
	
		e.printStackTrace();
	}
}
public void downloadFile(String url){
	URL urlLocation=null;
	try{
		String fileName = "/";
		if(!(url.contains("http")||url.contains("https")) )
			url ="http://"+url;
		try{
	    urlLocation=new URL(url);
		}
		catch(MalformedURLException e)
		{
			System.out.println("Wrong URL"+url.toString());
			return;
		}

		
		if((url.substring(url.lastIndexOf('/')+1).isEmpty())){

			fileName+=urlLocation.getHost(); 
		}
		
		else
			fileName+=url.substring(url.lastIndexOf('/')+1);

		System.out.println(fileName);
	    Path targetPath = new File(targetDirectory + fileName ).toPath();
		download(urlLocation,targetPath);		
}
catch(Exception e)
{
	e.printStackTrace();
}
}
public static void download(URL remoteURL, Path localPath) {
    BufferedInputStream in = null;
    FileOutputStream out = null;

    try {
        URLConnection conn = remoteURL.openConnection();
        int size = conn.getContentLength();
  
        in = new BufferedInputStream(remoteURL.openStream());
        out = new FileOutputStream(localPath.toString());
        byte data[] = new byte[10240];
        int count;
        double sumCount = 0.0;

        while ((count = in.read(data, 0, 10240)) != -1) {
            out.write(data, 0, count);

            sumCount += count;
            if (size > 0) {
                System.out.print(remoteURL.toString()+"Percentage: "+(int)(sumCount / size * 100.0) + "%\r");
            }
        }

    } catch (MalformedURLException e1) {
        e1.printStackTrace();
    } catch (IOException e2) {
        e2.printStackTrace();
    } finally {
        if (in != null)
            try {
                in.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        if (out != null)
            try {
                out.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
    }
    System.out.print(remoteURL.toString()+ "download in progress… DONE\n");
}
public static boolean isValidPath(String path) {

    try {

    	File file = new File(path);
    	if (!file.isDirectory())
    		return false;
    } catch (NullPointerException ex) {
        return false;
    }
   
    return true;
}
}
