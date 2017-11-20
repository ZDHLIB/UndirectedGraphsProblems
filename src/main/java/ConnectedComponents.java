import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ConnectedComponents {

    private static HashMap<Integer, Node> graph = new HashMap<Integer, Node>();
    private static Set<Integer> visitedNodes = new HashSet<Integer>();
    private static int N = 0;

    public HashMap<Integer, ArrayList<Integer> > findConnectedComponents(){
        HashMap<Integer, ArrayList<Integer> > resList = new HashMap<Integer, ArrayList<Integer> >();

        for(Map.Entry entity : graph.entrySet()) {
            Integer name = (Integer) entity.getKey();
            Queue<Integer> neighbours = ((Node) entity.getValue()).getNeighbours();

            N = (N<name)? name:N;

            if(!visitedNodes.contains(name)) {
                visitedNodes.add(name);

                Queue<Integer> queue = new LinkedList<Integer>();
                ArrayList<Integer> component = new ArrayList<Integer>();

                component.add(name);
                for(Integer str : neighbours){
                    queue.offer(str);
                }

                while(!queue.isEmpty()){
                    Integer n = queue.poll();
                    if(!visitedNodes.contains(n)) {
                        visitedNodes.add(n);
                        component.add(n);
                        Queue<Integer> neighbours2 = graph.get(n).getNeighbours();
                        for (Integer str : neighbours2) {
                            queue.offer(str);
                        }
                    }
                }
                Collections.sort(component);
                resList.put(component.get(0),component);
            }
        }
        return resList;
    }

    public void constructGraph(ArrayList<String> rawData){
        for(String line : rawData){
            Node node1;
            Node node2;
            String[] nodes = line.replaceAll("[ |\t]+"," ").split(" ");
            Integer x = Integer.valueOf(nodes[0]);
            Integer y = Integer.valueOf(nodes[1]);

            if(graph.containsKey(x)){
                node1 = graph.get(x);
            }else {
                node1 = new Node(x);
                graph.put(x, node1);
            }
            if(graph.containsKey(y)){
                node2 = graph.get(y);
            }else {
                node2 = new Node(y);
                graph.put(y, node2);
            }

            node1.addNeighbour(y);
            node2.addNeighbour(x);
        }
    }

    public static ArrayList<String> readData(String filePath){
        String line = "";
        ArrayList<String> rawData = new ArrayList<String>();
        try{
            File file = new File(filePath);
            if(!file.exists()){
                throw new Exception("File is not exist, please check the file path.");
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            while( (line=br.readLine()) != null ){
                rawData.add(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return rawData;
    }

    private class Node{
        private Integer id;  //Node name
        private Queue<Integer> neighbours;

        public Node(Integer id){
            this.id = id;
            neighbours = new LinkedList<Integer>();
        }

        public Integer getId() {
            return id;
        }

        public Queue<Integer> getNeighbours() {
            return neighbours;
        }

        public void addNeighbour(Integer neighbour){
            this.neighbours.offer(neighbour);
        }
    }

    public static void main(String[] arg){
        ConnectedComponents connectedComponents = new ConnectedComponents();

        String filePath = arg[0];

        //Read data from file
        ArrayList<String> rawData = readData(filePath);
        connectedComponents.constructGraph(rawData);
        HashMap<Integer, ArrayList<Integer> > resList = connectedComponents.findConnectedComponents();

        for(int i = 0; i <= N; i++) {
            if(visitedNodes.contains(i)) {
                ArrayList<Integer> tmp = resList.get(i);
                if(tmp!=null) {
                    System.out.print("[");
                    for (Integer str : tmp) {
                        System.out.print(str + " ");
                    }
                    System.out.print("]");
                    System.out.println();
                }
            } else {
                System.out.println("["+i+"]");
            }
        }
    }
}




















