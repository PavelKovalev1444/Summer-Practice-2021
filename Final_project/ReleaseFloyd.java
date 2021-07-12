
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.util.*;
public class ReleaseFloyd {

    private static int k_step = 0;
    private static int i_step = 0;
    private static int j_step = 0;

    private static ArrayList<Vector<Vector<Integer>>> states = new ArrayList <Vector<Vector<Integer>>>();
    private static ArrayList<Vector<Integer>> statesIJK = new ArrayList<Vector<Integer>>();

    public static Vector<Vector<Integer>> getState(){
        if(states.size() != 0){
            Vector<Vector<Integer>> st = states.get(states.size() - 1);
            states.remove(states.size() - 1);
            if(statesIJK.size() != 0) {
                Vector<Integer> tmpIjk = statesIJK.get(statesIJK.size() - 1);
                statesIJK.remove(statesIJK.size() - 1);
                i_step = tmpIjk.get(0);
                j_step = tmpIjk.get(1);
                k_step = tmpIjk.get(2);
                return st;
            }else{
                return null;
            }
        }
        return null;
    }

    public static void fillMatrix(Vector <Vector<Integer>> my_vec){
        int n = my_vec.size();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if(my_vec.get(i).get(j) == -1){
                    my_vec.get(i).set(j, 100000000);
                }
            }
        }
    }

    public static void run_step(Vector <Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph graph) {
        int n = my_vec.get(0).size();
        fillMatrix(my_vec);
        RecolorVertexDefault(graph);
        Vector <Vector<Integer>> tmp = new Vector <Vector<Integer>>();
        MatrixAdapterFabric.copyMatrix(tmp, my_vec);
        states.add(tmp);

        if(statesIJK.isEmpty()) {
            Vector<Integer> tmpVec = new Vector<Integer>();
            tmpVec.add(new Integer(0));
            tmpVec.add(new Integer(0));
            tmpVec.add(new Integer(0));
            statesIJK.add(tmpVec);
        }
        MakeStep(my_vec, my_vecSlashText, graph, n);

    }

    public static void run_all(Vector <Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph graph) {
        int n = my_vec.get(0).size();
        fillMatrix(my_vec);
        Vector <Vector<Integer>> tmp = new Vector <Vector<Integer>>();
        MatrixAdapterFabric.copyMatrix(tmp, my_vec);

        if(statesIJK.isEmpty()) {
            states.add(tmp);
            Vector<Integer> tmpVec = new Vector<Integer>();
            tmpVec.add(new Integer(0));
            tmpVec.add(new Integer(0));
            tmpVec.add(new Integer(0));
            statesIJK.add(tmpVec);
        }
        for (int k=0; k<n; ++k) {
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n; ++j)
                    MakeStepAll(my_vec,my_vecSlashText,graph,i,j,k);
        }

        TableAdapterFabric.genNewTable(my_vec, my_vecSlashText, graph, Main.jFieldTable);
    }

    private static void MakeStep(Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph graph, int n) {

        while(true) {
            int tmp1 = 0, tmp2 = 1;
            if(i_step >= n) {
                i_step = 0;
                k_step++;
            }
            if(k_step >= n) {
                JOptionPane.showMessageDialog(null,  "Algorithm is finish, check output matrix", "Promt", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (j_step = 0; j_step < n; j_step++) {
                tmp1 = my_vec.get(i_step).get(j_step);
                tmp2 = my_vec.get(i_step).get(k_step) + my_vec.get(k_step).get(j_step);
                if(tmp1 > tmp2){
                    break;
                }
            }

            System.out.println("ijk: " + i_step + " and " + j_step + " and " + k_step);
            System.out.println("collision: " + tmp1 + " and " + tmp2);
            if (tmp1 > tmp2) {
                if(j_step == n){
                    j_step -= 1;
                }
                Vector<Integer> tmpVec = new Vector<Integer>();
                tmpVec.add(new Integer(i_step));
                tmpVec.add(new Integer(j_step));
                tmpVec.add(new Integer(k_step));
                statesIJK.add(tmpVec);
                tmp1 = my_vec.get(i_step).get(j_step);
                tmp2 = Math.min(my_vec.get(i_step).get(j_step), my_vec.get(i_step).get(k_step) + my_vec.get(k_step).get(j_step));
                my_vec.get(i_step).set(j_step, tmp2);
                my_vecSlashText.get(i_step).set(j_step, TableAdapterFabric.getVecNames(graph).get(k_step));
                TableAdapterFabric.genNewTable(my_vec, my_vecSlashText, graph, Main.jFieldTable);
                RecolorVertex(graph, i_step, j_step, k_step);
                return;
            }
            else {
                i_step++;
            }
            System.out.println("end " + tmp1 + "," + tmp2);
        }
    }

    private static void MakeStepAll(Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph graph ,int i_step, int j_step, int k_step) {
        int tmp1 = my_vec.get(i_step).get(j_step);
        int tmp2 = Math.min(my_vec.get(i_step).get(j_step), my_vec.get(i_step).get(k_step) + my_vec.get(k_step).get(j_step));
        if(tmp1!=tmp2) {
            my_vec.get(i_step).set(j_step, tmp2);
            my_vecSlashText.get(i_step).set(j_step, TableAdapterFabric.getVecNames(graph).get(k_step));
        }
    }

    public static void RecolorVertexDefault(mxGraph graph) {
        for (Object c : graph.getChildVertices(graph.getDefaultParent())) {
            mxCell timeCell;
            timeCell = (mxCell) c;
            timeCell.setStyle("defaultStyle");
        }
        graph.refresh();
    }

    public static void RecolorVertex(mxGraph graph, int i_step, int j_step, int k_step){

        for (Object c : graph.getChildVertices(graph.getDefaultParent())) {
            mxCell timeCell;
            int timeCellId;
            timeCell = (mxCell) c;
            StringBuilder time_string = new StringBuilder( timeCell.getId());
            time_string.deleteCharAt(time_string.length()-1);
            timeCellId = Integer.parseInt(time_string.toString());
            if(i_step == timeCellId) {
                timeCell.setStyle("fillColor=green");
            }
            if(j_step == timeCellId) {
                timeCell.setStyle("fillColor=green");
            }
            if(k_step == timeCellId) {
                timeCell.setStyle("fillColor=red");
            }

        }
        graph.refresh();
    }

    public static void algo(Vector <Vector<Integer>> my_vec) {
        int n = my_vec.get(0).size();
        fillMatrix(my_vec);
        Vector <Vector<Integer>> tmp = new Vector <Vector<Integer>>();
        MatrixAdapterFabric.copyMatrix(tmp, my_vec);
        for (int k=0; k<n; ++k) {
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n; ++j)
                    my_vec.get(i).set(j, Math.min(my_vec.get(i).get(j), my_vec.get(i).get(k) + my_vec.get(k).get(j)));
        }
    }

    public static void recolorUndo(mxGraph graph){
        RecolorVertexDefault(graph);
        RecolorVertex(graph, i_step, j_step, k_step);
        if(states.isEmpty()){
            RecolorVertexDefault(graph);
        }
    }

}