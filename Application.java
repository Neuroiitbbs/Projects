import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
/**Nice Work.
 * I changed the first JFrame to setSize to prevent Overlap
 * I have added two methods:
 * 1. saveBoard() to save the state of the game[necessary for future analysis, in all ogical senses]
 * 2. recreateBoard(), which replays the game, but with a catch-currently it shows only the last state. Try to modify it so that
 * it relays the game exactly, and if unable, ask for help :)
   */

public class Application extends JFrame implements ActionListener {
    JPanel jPanel = new JPanel();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    int side = 40;
    JButton b[][];
    ArrayList<int[]> people = new ArrayList();
    ArrayList<int[]> kings = new ArrayList();
    public static void main(String[] args) {
        Application obj = new Application();
    }
    private Application() {
        super("Conway's Game of Life");
        setSize(800, 900);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jPanel.setLayout(new BorderLayout());
        jPanel1.setLayout(new GridLayout(side, side, 0, 0));
        jPanel1.setSize(new Dimension(800, 600));
        b = new JButton[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                b[i][j] = new JButton();
                b[i][j].setBackground(Color.WHITE);
                b[i][j].addActionListener(this);
                jPanel1.add(b[i][j]);
            }
        }
        jPanel2.setLayout(new BorderLayout());
        JButton start = new JButton();
        start.setText("Start");
        JButton next = new JButton();
        next.setText("Next");
        JButton reCreate=new JButton();
        reCreate.setText("Recreate");
        jPanel2.add(next, BorderLayout.EAST);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                play();
            }
        });
        jPanel2.add(start, BorderLayout.WEST);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                init();
            }
        });
        jPanel2.add(reCreate,BorderLayout.CENTER);
        reCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                go();
            }
        });
        jPanel2.setPreferredSize(new Dimension(800, 300));
        jPanel.add(jPanel1, BorderLayout.NORTH);
        jPanel.add(jPanel2, BorderLayout.SOUTH);
        add(jPanel);
        setVisible(true);
        System.out.println("Running");
    }
    public void go()//ToDo: Can you complete this method so that the game is replayed? Hint: You need to use the timer class and the sayHello class
    //currently commented out
    {
        System.out.println("I'm called");
        try
        {
            File file = new File("D:/Documents/states.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            int i,j,p=0;
            while ((str = br.readLine()) != null)
            {
                System.out.println(str);
                String arr[]=str.split(" ");
                
                for(i=0;i<side;i++)
                    for(j=0;j<side;j++)
                        b[i][j].setBackground(Color.WHITE);
                // And From your main() method or any other method
                //Timer timer = new Timer();
                //timer.schedule(new SayHello(), 0, 2000);
                for(i=0;i<arr.length;i++)
                {
                    String o[]=arr[i].split(",");
                    b[Integer.parseInt(o[0])][Integer.parseInt(o[1])].setBackground(Color.BLACK);
                }
            }
            br.close();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    private void init() {
        try
        {
            File file=new File("D:/Documents/states.txt");//Same path as before
            if(!Files.deleteIfExists(file.toPath()))
            {
                System.out.println("Something wrong...Can't reCreate game");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                b[i][j].removeActionListener(this);
                if (b[i][j].getBackground() == Color.BLACK) {
                    int temp[] = {i, j};
                    people.add(temp);
                }
            }
        }
    }

    private int neighbours(int i, int j) {
        int count = 0;
        if (i >= 0 && i < side && (j + 1) >= 0 && (j + 1) < side && b[i][j + 1].getBackground() == Color.BLACK) count++;
        if (i >= 0 && i < side && (j - 1) >= 0 && (j - 1) < side && b[i][j - 1].getBackground() == Color.BLACK) count++;
        if ((i + 1) >= 0 && (i + 1) < side && (j + 1) >= 0 && (j + 1) < side && b[i + 1][j + 1].getBackground() == Color.BLACK)
            count++;
        if ((i + 1) >= 0 && (i + 1) < side && (j - 1) >= 0 && (j - 1) < side && b[i + 1][j - 1].getBackground() == Color.BLACK)
            count++;
        if ((i - 1) >= 0 && (i - 1) < side && (j + 1) >= 0 && (j + 1) < side && b[i - 1][j + 1].getBackground() == Color.BLACK)
            count++;
        if ((i - 1) >= 0 && (i - 1) < side && (j - 1) >= 0 && (j - 1) < side && b[i - 1][j - 1].getBackground() == Color.BLACK)
            count++;
        if ((i + 1) >= 0 && (i + 1) < side && j >= 0 && j < side && b[i + 1][j].getBackground() == Color.BLACK) count++;
        if ((i - 1) >= 0 && (i - 1) < side && j >= 0 && j < side && b[i - 1][j].getBackground() == Color.BLACK) count++;

        return count;
    }

    private void newLife(int x, int y) {
        if (x >= 0 && x < side && (y + 1) >= 0 && (y + 1) < side && b[x][y + 1].getBackground() == Color.WHITE)
            enqueue(x, y + 1);
        if (x >= 0 && x < side && (y - 1) >= 0 && (y - 1) < side && b[x][y - 1].getBackground() == Color.WHITE)
            enqueue(x, y - 1);
        if ((x + 1) >= 0 && (x + 1) < side && (y + 1) >= 0 && (y + 1) < side && b[x + 1][y + 1].getBackground() == Color.WHITE)
            enqueue(x + 1, y + 1);
        if ((x + 1) >= 0 && (x + 1) < side && (y - 1) >= 0 && (y - 1) < side && b[x + 1][y - 1].getBackground() == Color.WHITE)
            enqueue(x + 1, y - 1);
        if ((x - 1) >= 0 && (x - 1) < side && (y + 1) >= 0 && (y + 1) < side && b[x - 1][y + 1].getBackground() == Color.WHITE)
            enqueue(x - 1, y + 1);
        if ((x - 1) >= 0 && (x - 1) < side && (y - 1) >= 0 && (y - 1) < side && b[x - 1][y - 1].getBackground() == Color.WHITE)
            enqueue(x - 1, y - 1);
        if ((x + 1) >= 0 && (x + 1) < side && y >= 0 && y < side && b[x + 1][y].getBackground() == Color.WHITE)
            enqueue(x + 1, y);
        if ((x - 1) >= 0 && (x - 1) < side && y >= 0 && y < side && b[x - 1][y].getBackground() == Color.WHITE)
            enqueue(x - 1, y);
    }

    private void enqueue(int p, int q) {
        int n = neighbours(p, q);
        int temp[] = {p, q};
        if (n == 3 && !listContains(kings, temp)) {
            kings.add(temp);
        }
    }

    private void play() {
        //while (!people.isEmpty()) {
            kings.clear();
            int n = 0;
            int x = 0, y = 0;
            for (int i = 0; i < people.size(); i++) {
                x = people.get(i)[0];
                y = people.get(i)[1];
                n = neighbours(x, y);
                if (n == 2 || n == 3) {
                    kings.add(people.get(i));
                }

                newLife(x, y);
            }
            people.clear();
            people = (ArrayList<int[]>) kings.clone();

            int temp[] = {0, 0};
            for (int i = 0; i < side; i++) {
                for (int j = 0; j < side; j++) {
                    temp[0] = i;
                    temp[1] = j;
                    if (listContains(kings, temp)) {
                        b[i][j].setBackground(Color.BLACK);
                    } else {
                        b[i][j].setBackground(Color.WHITE);
                    }
                }
            }
            saveBoard();
            //break;
        //}
    }
    private void saveBoard()
    {
        //System.out.println("I got called");
        int i,j;
        try{
            FileWriter fw = new FileWriter("D:/Documents/states.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);  
            StringBuilder sb=new StringBuilder();
            for(i=0;i<side;i++)
            {
                for(j=0;j<side;j++)
                {
                    if(b[i][j].getBackground()==Color.BLACK)
                    {
                        sb.append(i+","+j+" ");
                    }
                }
            }
            bw.write(sb.toString());
            bw.newLine();
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    private boolean listContains(ArrayList<int[]> list, int[] query) {
        for (int[] arr : kings) {
            if (arr[0] == query[0] && arr[1] == query[1]) {
                return true;
            }
        }

        return false;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton temp = ((JButton) (actionEvent.getSource()));
        if (temp.getBackground() == Color.WHITE) temp.setBackground(Color.BLACK);
        else temp.setBackground(Color.WHITE);
    }
    /*class SayHello extends TimerTask {
        public void run() {
           System.out.println("Hello World!"); 
        }
    }*/
}