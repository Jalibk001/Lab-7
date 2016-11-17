package toystopinventorysystem;

import static java.awt.JobAttributes.DestinationType.FILE;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;


/**
 *
 * @author Fahad Satti
 */
public class ToyStopInventoryManagementSystem {
    ToyStopService tsService = new ToyStopService();
    public void init(){
        
        tsService.initEmployees();
        tsService.initStores();
        tsService.initToys();
        System.out.println("Init complete");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        
        ToyStopInventoryManagementSystem tsims = new ToyStopInventoryManagementSystem();
        try
        {
            File fl1=new File("StoreData.ser");  //serialization files
            File fl2=new File("Employee.ser");
            if(fl1.exists()&&fl2.exists())
            {
                FileInputStream filein=new FileInputStream("StoreData.ser");
                ObjectInputStream outputfl=new ObjectInputStream(filein);
                tsims.tsService.stores=(ArrayList) outputfl.readObject();
                outputfl.close();
                filein.close();
                
                filein= new FileInputStream("Employee.ser");
                outputfl= new ObjectInputStream(outputfl);
                tsims.tsService.employees=(ArrayList)outputfl.readObject();
                outputfl.close();
                filein.close();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
        catch(ClassNotFoundException c)
        {
            System.out.println("No Data available");
            c.printStackTrace();
            return;
        }
        if(tsims.tsService.stores.isEmpty())
        {
            System.out.println("File Empty so initializing");
            tsims.init();
        }
        
        tsims.showMenu();
        System.out.print("Your Choice: ");
        Scanner reader=new Scanner(System.in);
        int choice=reader.nextInt();
        switch(choice)
        {
            case 0:
                tsims.save(tsims.tsService.stores,tsims.tsService.employees );
                break;
            case 1:
                tsims.printAll();
                break;
            case 2:
                int id=tsims.tsService.addStore();
                System.out.println("New store added, ID: "+id);
                break;
            case 3:
                int EmpID=tsims.tsService.addEmployee();
                System.out.println("New Employee added, ID: "+EmpID);
                break;
            case 4:
                
                Toy newToy = new Toy();
                newToy.setUID(Util.getSaltNum(-1));
                newToy.setMinAge(Util.getSaltNum(1));
                newToy.setMaxAge(Util.getSaltNum(18));
                newToy.setPrice(Util.getSaltNum(1000));
                newToy.setName(Util.getSaltAlphaString());
                newToy.setAddedOn(LocalDateTime.now());
                
                Random st=new Random();
                int index = st.nextInt(tsims.tsService.stores.size());
                Store selectedStore = (Store)tsims.tsService.stores.get(index);
                selectedStore.addToy(newToy);
                break;
            default:
                break;
                
        }

        
    }
    void save(ArrayList<Store>stdata,ArrayList<Employee>empdata)
    {
        try
        {
        FileOutputStream StOut=new FileOutputStream("StoreData");
        ObjectOutputStream outp=new ObjectOutputStream(StOut);
        outp.writeObject(stdata);
        
        FileOutputStream Eout=new FileOutputStream("Employee.ser");
        outp=new ObjectOutputStream(Eout);
        outp.writeObject(empdata);
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private void showMenu() {
        System.out.println("Welcome to Toy Stop Inventory Management System");
        System.out.println("Enter 1 to show all data");
        System.out.println("Enter 2 to add a new Store");
        System.out.println("Enter 3 to add a new Employee");
        System.out.println("Enter 4 to add a new Toy");
        System.out.println("Enter 0 to save state");
    }

    private void printAll() {
        System.out.println(this.tsService.stores);
    }
    
}
