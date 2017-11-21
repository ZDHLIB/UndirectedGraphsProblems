import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ClosenessCentrality {

    private static HashMap<Integer, Node> graph = new HashMap<Integer, Node>();
    private static Set<Integer> visitedNodes = new HashSet<Integer>();
    private static int N = 0;

    public HashMap<Integer, BigDecimal> calClosenessCentrality(){
        HashMap<Integer, BigDecimal> res = new HashMap<Integer, BigDecimal>();
        for(Map.Entry entity : graph.entrySet()) {
            Node node = (Node) entity.getValue();
            res.put(node.getId(), node.getDis().divide(BigDecimal.valueOf(N), 6, BigDecimal.ROUND_HALF_UP) );
        }
        return res;
    }

    private void calDistance(Node node){
        int d = 1, layer = 0;
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(node.getId());
        layer = 1;
        while(!queue.isEmpty()){
            if(layer>0){
                layer--;
            }else{
                layer = queue.size();
                d++;
            }
            Integer name = queue.poll();
            if(!visitedNodes.contains(name)) {
                visitedNodes.add(name);
            }
            Queue<Integer> neighbours = graph.get(name).getNeighbours();
            for (Integer id : neighbours) {
                if(!visitedNodes.contains(id)) {
                    visitedNodes.add(id);
                    queue.offer(id);
                    node.updateDis(id, d);
                }
            }

        }
        visitedNodes.clear();

    }

    private void constructAllDis(){
        for(Map.Entry entity : graph.entrySet()) {
            Integer id = (Integer) entity.getKey();
            N = (N<id)? id:N;
            Node node = (Node) entity.getValue();
            calDistance(node);
        }
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
        private BigDecimal dis;

        public Node(Integer id){
            this.id = id;
            neighbours = new LinkedList<Integer>();
            dis = new BigDecimal(0);
        }

        public void updateDis(Integer id, Integer d){
            dis = dis.add(BigDecimal.valueOf(1).divide(BigDecimal.valueOf(d), 6, BigDecimal.ROUND_HALF_UP));
        }

        public Integer getId() {
            return id;
        }

        public BigDecimal getDis() {
            return dis;
        }

        public Queue<Integer> getNeighbours() {
            return neighbours;
        }

        public void addNeighbour(Integer neighbour){
            this.neighbours.offer(neighbour);
        }
    }

    public static void main(String[] arg){
        ClosenessCentrality closenessCentrality = new ClosenessCentrality();

        String filePath = arg[0];

        //Read data from file
        ArrayList<String> rawData = readData(filePath);
        closenessCentrality.constructGraph(rawData);
        closenessCentrality.constructAllDis();
        HashMap<Integer, BigDecimal> res = closenessCentrality.calClosenessCentrality();
        for(int i=0; i <= N; i++){
            System.out.print("["+i+"] ");
            if(res.containsKey(i)){
                System.out.println(res.get(i));
            }else{
                System.out.println("0.000000");
            }
        }
    }
}
