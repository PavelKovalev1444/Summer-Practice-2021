import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.view.mxGraph;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main extends JFrame
{


    private final double widthVertex = 80;
    private final double heightVertex = 30;
    private static final long serialVersionUID = -2707712944901661771L;


    public Main()
    {
        super("Graph vizualizator");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mxGraphOutline mxGraphOutline = new mxGraphOutline(graphComponent);

        graphComponent.setEnabled(false);

        graph.getModel().beginUpdate(); // начали обновлять
        try
        {
            Object v2 = graph.insertVertex(parent, null, "World!", 0, 270,80, 30);
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);

        }
        finally
        {
            graph.getModel().endUpdate(); // закончили
        }


        MouseAdapter moveVertexAdapter = new MouseAdapter() {
            int distanceX, distanceY;
            boolean moves = false;
            double width, height;
            mxCell cell;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (moves){
                    mxGeometry geometry = cell.getGeometry();
                    if (cell.isVertex()){
                        graph.getModel().beginUpdate();
                        graph.getModel().setGeometry(cell, new mxGeometry(geometry.getX() + (e.getX() - distanceX), geometry.getY() + (e.getY() - distanceY), width, height));
                        distanceX = e.getX();
                        distanceY = e.getY();
                        graph.getModel().endUpdate();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cell = (mxCell) mxGraphOutline.getGraphComponent().getCellAt(e.getX(), e.getY());
                if (cell != null){
                    mxGeometry geometry = cell.getGeometry();
                    width = geometry.getWidth();
                    height = geometry.getHeight();
                    distanceX = e.getX();
                    distanceY = e.getY();
                    moves = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                distanceX = 0;
                distanceY = 0;
                moves = false;
            }
        };

        MouseAdapter addVertexMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                graph.getModel().beginUpdate(); // начали обновлять
                try
                {
                    Object v2 = graph.insertVertex(parent, null, "Added", e.getX() - widthVertex / 2, e.getY() - heightVertex / 2, widthVertex, heightVertex);
                }
                finally
                {
                    graph.getModel().endUpdate(); // закончили
                }
            }
        };

        MouseAdapter deleteCellMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                mxCell cell = (mxCell) mxGraphOutline.getGraphComponent().getCellAt(e.getX(), e.getY());
                if (cell != null){
                    graph.getModel().beginUpdate(); // начали обновлять
                    try
                    {
                        cell.removeFromParent();
                        graph.refresh();
                    }
                    finally
                    {
                        graph.getModel().endUpdate(); // закончили
                    }
                }
            }
        };

        graphComponent.getGraphControl().addMouseListener(moveVertexAdapter);
        graphComponent.getGraphControl().addMouseMotionListener(moveVertexAdapter);



        JPanel panelCenter = new JPanel();
        JPanel panelRightRight = new JPanel();
        JPanel panelRightRightDown = new JPanel();
        panelRightRight.setMaximumSize(new Dimension(400, 30));

        BoxLayout boxlayoutVerticalAll = new BoxLayout(panelCenter, BoxLayout.Y_AXIS);
        BoxLayout boxlayoutAll = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
        BoxLayout boxlayoutRightRight = new BoxLayout(panelRightRight, BoxLayout.X_AXIS);
        BoxLayout boxlayoutRightRightDown = new BoxLayout(panelRightRightDown, BoxLayout.X_AXIS);

        panelCenter.setLayout(boxlayoutVerticalAll);
        panelRightRight.setLayout(boxlayoutRightRight);
        panelRightRightDown.setLayout(boxlayoutRightRightDown);
        getContentPane().setLayout(boxlayoutAll);

        JButton jButtonNewGraph = new JButton("New_Graph");
        JToggleButton jButtonAdd = new JToggleButton("Add");
        JToggleButton jButtonRemove = new JToggleButton("Remove");


        JButton jButtonStep = new JButton("Play one step");
        JButton jButtonAll = new JButton("Play all");
        JButton jButtonLoad = new JButton("Loading");


        JTextArea jTextMatrixSmeshznosti = new JTextArea("Adjacency matrix:");
        jTextMatrixSmeshznosti.setMaximumSize(new Dimension(130, 20));
        JTable jFieldTable = new JTable(2,2);
        jFieldTable.setMaximumSize(new Dimension(300, 300));
        jFieldTable.setRowHeight(30);
        jFieldTable.setRowHeight(20, 20);
        jFieldTable.setIntercellSpacing(new Dimension(10, 10));
        jFieldTable.setGridColor(Color.white);
        jFieldTable.setShowVerticalLines(false);





        panelRightRight.add(jButtonNewGraph);
        panelRightRight.add(jButtonLoad);
        panelRightRight.add(jButtonAdd);
        panelRightRight.add(jButtonRemove);


        panelRightRightDown.add(jButtonStep);
        panelRightRightDown.add(jButtonAll);


        panelCenter.add(panelRightRight);
        panelCenter.add(jTextMatrixSmeshznosti);
        panelCenter.add(jFieldTable);
        panelCenter.add(panelRightRightDown);


        jButtonAdd.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(itemEvent.getStateChange()==ItemEvent.SELECTED){
                    jButtonRemove.setSelected(false);
                    graphComponent.getGraphControl().addMouseListener(addVertexMouseAdapter);
                } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED){
                    graphComponent.getGraphControl().removeMouseListener(addVertexMouseAdapter);
                }
            }
        });

        jButtonRemove.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange()==ItemEvent.SELECTED){
                    jButtonAdd.setSelected(false);
                    graphComponent.getGraphControl().addMouseListener(deleteCellMouseAdapter);
                } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED){
                    graphComponent.getGraphControl().removeMouseListener(deleteCellMouseAdapter);
                }
            }
        });


        getContentPane().add(graphComponent);
        getContentPane().add(panelCenter);
    }

    public static void main(String[] args)
    {
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 2 - 375, dimension.height / 2 - 250, 750, 500);
        frame.setVisible(true);
    }

}