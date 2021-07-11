import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableModel;

public class TableAdapterFabric {
    public static Vector<String> getVecNames(mxGraph my_g) {
        Vector<String> out_vec = new Vector<String>();
        mxCell timeCell;
        for (Object c : my_g.getChildVertices(my_g.getDefaultParent())) {
            timeCell = (mxCell) c;
            String time_string = (String) my_g.getModel().getValue(timeCell);
            //System.out.println(time_string);
            out_vec.addElement(time_string);
        }
        // System.out.println(out_vec);
        return out_vec;
    }

//Для названий вершин

    public static int getMaxLengthElementString(Vector<String> my_str_vec, int bool_int) {
        int out_int = bool_int;
        for(int i = 0; i < my_str_vec.size(); i++) {
            if(my_str_vec.get(i).length()>bool_int) {
                out_int = my_str_vec.get(i).length();
            }
        }
        return out_int;
    }

//Для элементов

    public static int getMaxLengthElement(Vector<Vector<Integer>> my_vec) {
        Integer max = 0;
        for(int i = 0; i < my_vec.size(); i++) {
            for (int j = 0; j < my_vec.size(); j++) {
                if((my_vec.get(i).get(j) < 100000000)&&(my_vec.get(i).get(j) > max))
                    max = my_vec.get(i).get(j);
            }
        }
        String time_string = max.toString();
        return time_string.length();
    }

    public static int getMaxLengthElementFloyd(Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText) {
        Integer max = 0;
        for(int i = 0; i < my_vec.size(); i++) {
            for (int j = 0; j < my_vec.size(); j++) {
                int my_vec_l = my_vec.get(i).get(j).toString().length();
                int my_vecSlashText_l = my_vecSlashText.get(i).get(j).length();
                int tmp = my_vec_l + my_vecSlashText_l + 1;
                if(tmp > max)
                    max = tmp;
            }
        }
        String time_string = max.toString();
        return time_string.length();
    }

//Для обновления таблицы

    public static void addTableUpdate(Vector<Vector<Integer>> my_vec, JTextArea my_jta, mxGraph my_g) {
        my_jta.setText(null);
        Vector<String> str_vec = getVecNames(my_g);
        int l_loc;

        l_loc = getMaxLengthElement(my_vec);
        l_loc = getMaxLengthElementString(str_vec, l_loc);

        addTableElement(my_jta, "", l_loc);
        for(int i = 0; i < my_vec.size(); i++) {
            addTableElement(my_jta, str_vec.get(i), l_loc);
        }
        my_jta.append("\n");
        for(int i = 0; i < my_vec.size(); i++) {
            addTableElement(my_jta, str_vec.get(i), l_loc);
            for(int j = 0; j < my_vec.size(); j++) {
                if((my_vec.get(i).get(j) == 100000000)||(my_vec.get(i).get(j) == -1))
                    addTableElement(my_jta, "N", l_loc);
                else
                    addTableElement(my_jta, my_vec.get(i).get(j).toString(), l_loc);
                //my_jta.append(my_vec.get(i).get(j).toString() + " ");
            }
            my_jta.append("\n");
        }
    }

    public static void addTableUpdateFloyd(Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, JTextArea my_jta, mxGraph my_g) {
        my_jta.setText(null);
        Vector<String> str_vec = getVecNames(my_g);
        int l_loc;

        l_loc = getMaxLengthElementFloyd(my_vec, my_vecSlashText);
        l_loc = getMaxLengthElementString(str_vec, l_loc);

        addTableElement(my_jta, "", l_loc);
        for(int i = 0; i < my_vec.size(); i++) {
            addTableElement(my_jta, str_vec.get(i), l_loc);
        }
        my_jta.append("\n");
        for(int i = 0; i < my_vec.size(); i++) {
            addTableElement(my_jta, str_vec.get(i), l_loc);
            for(int j = 0; j < my_vec.size(); j++) {
                if((my_vec.get(i).get(j) == 100000000)||(my_vec.get(i).get(j) == -1))
                    addTableElement(my_jta, "N", l_loc);
                else {
                    if (my_vecSlashText.get(i).get(j).length() > 0) {
                        addTableElement(my_jta, my_vec.get(i).get(j).toString() + "/" + my_vecSlashText.get(i).get(j), l_loc);
                    }
                    else {
                        addTableElement(my_jta, my_vec.get(i).get(j).toString(), l_loc);
                    }
                }
            }
            my_jta.append("\n");
        }
    }

//Для печати

    public static void addTableElement(JTextArea my_jta, String my_str, int l) {
        int insert_l = my_str.length();
        my_jta.append(my_str);
        for(; insert_l<l; insert_l++) {
            my_jta.append("  ");
        }
        my_jta.append("  ");
    }

//Для Флойда

    public static void resetElementTableUpdateFloyd(Vector<Vector<String>> my_vec, String vertexName, int i, int j) {
        my_vec.get(i).set(j, vertexName);
    }
// // // НОВАЯ ТАБЛИЦА

    public static void genNewTable(Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph my_g, JTable my_jtable) {
        Vector<Vector<String>> str_vec = genTableVector(my_vec, my_vecSlashText, my_g);
        addTableUpdateElements(str_vec, my_jtable);
    }

    public static Vector<Vector<String>> genTableVector (Vector<Vector<Integer>> my_vec, Vector<Vector<String>> my_vecSlashText, mxGraph my_g) {
        Vector<Vector<String>> answer = new Vector<Vector<String>>();

        int size = my_vec.size()+1;
        //Генерим один пустой элемент + заголовки

        answer.addElement(new Vector<String>());
        answer.get(0).addElement("");
        Vector<String> tmp_str1 = getVecNames(my_g);
        for(int i = 1; i < size; i++) {
            answer.get(0).addElement(tmp_str1.get(i-1));
        }

        //Циклично вставляем сначала букву потом данные

        for(int i = 1; i < size; i++) {
            answer.addElement(new Vector<String>());
            answer.get(i).addElement(tmp_str1.get(i-1));
            for(int j = 1; j < size; j++) {
                if (my_vec.get(i-1).get(j-1) == 100000000){
                    answer.get(i).addElement("N");
                }
                else {
                    if(my_vecSlashText.get(i-1).get(j-1).length()!=0)
                        answer.get(i).addElement(my_vec.get(i-1).get(j-1).toString() + "/" + my_vecSlashText.get(i-1).get(j-1));
                    else
                        answer.get(i).addElement(my_vec.get(i-1).get(j-1).toString());
                }


            }
        }
        return answer;
    }

    public static void addTableUpdateElements(Vector<Vector<String>> final_vec, JTable my_jtable) {
        int size = final_vec.size();
        JTable tmp_jtable = new JTable(size, size);
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmp_jtable.setValueAt(final_vec.get(i).get(j),i,j);
            }
        }
        TableModel tmp_tableModel = tmp_jtable.getModel();
        my_jtable.setModel(tmp_tableModel);
        my_jtable.revalidate();
    }
}