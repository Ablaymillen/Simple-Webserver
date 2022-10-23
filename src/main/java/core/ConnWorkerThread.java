package core;

import java.io.*;
import java.net.Socket;

import java.nio.charset.StandardCharsets;

public class ConnWorkerThread extends Thread {

    private Socket socket;
    private HttpRequest request;
    private ServerFunctions serverFunctions = new ServerFunctions();
    public ConnWorkerThread(Socket socket, HttpRequest request){
        this.socket = socket;
        this.request = request;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String resp = "";
        try {
            // reading from the socket
            inputStream = socket.getInputStream();
            // writing to the socket
            outputStream = socket.getOutputStream();

            System.out.println("Got request:");
            System.out.println(request.toString());
            System.out.flush();
            String req = request.toString();
            String filename = "";
            if (req.contains("create/")){
                filename = System.getProperty("user.dir")+"/files/"+serverFunctions.createFileName(req)+".txt";
                System.out.println("FILENAME TO CREATE IS " + filename);

                try {
                    File f = new File(filename);
                    if (f.createNewFile()) {
                        resp += "Successfully created file: " + f.getName();
                    } else {
                        resp += "File " + f.getName() + " already exists:";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(req.contains("delete/")){
                filename = System.getProperty("user.dir")+"/files/"+serverFunctions.deleteFileName(req)+".txt";
                System.out.println("FILENAME TO DELET IS " + filename);
                try
                {
                    File f= new File(filename);
                    if(f.delete()){
                        resp += f.getName() + " file successfully deleted";   //getting and printing the file name
                    }
                    else{
                        resp += "Cannot find file:";

                    }
                }
                catch(Exception e){e.printStackTrace();}
            }
            else if(req.contains("square/")){
                double[] res = serverFunctions.square(req);
                resp += "Square of of " + res[0] + " is: " + res[1];
            }
            else if(req.contains("add/")){
                double[] res = serverFunctions.add(req);
                resp += res[0] + " + " + res[1] + " = " + res[2];
            }



            // To send response back to the client.
            PrintWriter output = new PrintWriter(socket.getOutputStream());

            // We are returning a simple web page now.
            // http version status code
            output.println("HTTP/1.1 200 OK");
            // content type and encoding
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            // html code
            output.println("<html>");
            output.println("<head><title>Simple Server</title></head>");
            output.println("<body><p>Hello, world!</p>");
            output.println(resp);
            output.println("<body><p>Simple webserver</p></body>");
            output.println("</html>");
            output.flush();


            System.out.println("Server connection finished");
        }
        // Closing inputStream, outputStream and Socket
        catch (IOException e) {
            System.out.println("Communication error");
            e.printStackTrace();

        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
