package rs.ac.bg.fon.CinemaServer.threads;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    static boolean end = true;
    ServerSocket serverSocket;
    List<ProcessRequests> admins;
    
    public Server() {
        admins = new ArrayList<>();
    }
    
    public void stopServer(){
        try {
            serverSocket.close();
            end = true;
            for(ProcessRequests admin: admins){   
                admin.interrupt();
                admin.getSocket().close();
            }

            admins = new ArrayList<>();
            System.out.println("Server closed!");
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }

    @Override
    public void run() {
        try{
            serverSocket=new ServerSocket(9000);
            while(!end){
                System.out.println("Waiting for connection...");            
                Socket socket=serverSocket.accept();
                System.out.println("Connected!");
                handleClient(socket);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
            
    }
    
    

    private void handleClient(Socket socket) throws Exception {
        ProcessRequests processRequests = new ProcessRequests(this, socket);
        admins.add(processRequests);
        processRequests.start();
    }
    
    public static void setEnd(boolean end){
        Server.end=end;
    }
    
    public static boolean getEnd(){
        return end;
    }
}

