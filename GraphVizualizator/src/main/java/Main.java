import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.view.mxGraph;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;


public class Main extends JFrame
{

    static JFrame jFrame;
    private  final  String[][]  menuFile =
            {{"Граф"     ,  "Ф",  "", ""},
                    {"Новый граф"  ,  "О", "O", ""},
                    {"Открыть граф из файла",  "С", "S", ""}};
    private  final  String[][]  menuEdit =
            {{"Другое" , "Р",  "", ""},
                    {"Справка"  , "В", "X", ""},
                    {"О программе", "К", "C", ""}};
    private final double widthVertex = 80;
    private final double heightVertex = 30;
    private static final long serialVersionUID = -2707712944901661771L;

    private JMenu createMenuItems(final String[][] items)
    {
        // Создание выпадающего меню
        JMenu menu = new JMenu(items[0][0]);
        menu.setMnemonic(items[0][1].charAt(0));
        for (int i = 1; i < items.length; i++) {
            // пункт меню "Открыть"
            JMenuItem item = new JMenuItem(items[i][0]);
            item.setMnemonic(items[i][1].charAt(0)); // русская буква
            // установим клавишу быстрого доступа (латинская буква)
            item.setAccelerator(KeyStroke.getKeyStroke(items[i][2].charAt(0),
                    KeyEvent.CTRL_MASK));
            if (items[i][3].length() > 0)
                item.setIcon(new ImageIcon(items[i][3]));
            menu.add(item);
        }
        return menu;
    }

    private JMenu createSubmenus()
    {
        JMenu text = new JMenu("Текст");
        // и несколько вложенных меню
        JMenu style = new JMenu("Стиль");
        JMenuItem bold = new JMenuItem("Жирный");
        JMenuItem italic = new JMenuItem("Курсив");
        JMenu font = new JMenu("Шрифт");
        JMenuItem arial = new JMenuItem("Arial");
        JMenuItem times = new JMenuItem("Times");
        font.add(arial); font.add(times);
        // размещаем все в нужном порядке
        style.add(bold);
        style.add(italic);
        style.addSeparator();
        style.add(font);
        text.add(style);
        return text;
    }






    public boolean connectionVertex(mxCell cell1, mxCell cell2){
        boolean check = false;
        for (int i = 0; i < cell1.getEdgeCount(); i++) {
            mxICell target = ((mxCell) cell1.getEdgeAt(i)).getTarget();
            if (target == cell2){
                check = true;
            }
        }
        return check;
    }


    public Main()
    {
        super("Graph vizualizator");

        JMenuBar menuBar = new JMenuBar();
        // Создание меню "Файл"
        menuBar.add(createMenuItems(menuFile));
        // Создание меню "Редактирование"
        menuBar.add(createMenuItems(menuEdit));

        //menuBar.add(createSubmenus());

        // JMenuBar использует блочное расположение (заполнитель вполне уместен)
        menuBar.add(Box.createHorizontalGlue());
        // Разместим в строке меню не выпадающее меню, а надпись со значком
        JLabel exit = new JLabel(new ImageIcon(""));
        exit.setText("Выход");
        exit.setBorder(BorderFactory.createEtchedBorder());
        //menuBar.add(exit);

        // поместим меню в наше окно
        setJMenuBar(menuBar);
        // выводим окно на экран
        //setSize(300, 200);
        //setVisible(true);

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mxGraphOutline mxGraphOutline = new mxGraphOutline(graphComponent);
        mxParallelEdgeLayout layout = new mxParallelEdgeLayout(graph);
        graph.refresh();
        graphComponent.setEnabled(false);

        graph.getModel().beginUpdate(); // начали обновлять
        try
        {
            Object v2 = graph.insertVertex(parent, null, "World!", 0, 270,80, 30);
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
            graph.insertEdge(parent, null, "6", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate(); // закончили
        }

        MouseAdapter addEdgeMouseAdapter = new MouseAdapter() {
            mxCell firstCell = null;
            mxCell secondCell = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (firstCell == null){
                    firstCell = (mxCell) mxGraphOutline.getGraphComponent().getCellAt(e.getX(), e.getY());
                    if (firstCell != null){
                        if (firstCell.isVertex()){
                            graph.getModel().beginUpdate(); // начали обновлять
                            try
                            {
                                firstCell.setStyle("fillColor=green");
                                graph.refresh();
                            }
                            finally
                            {
                                graph.getModel().endUpdate(); // закончили
                            }
                        } else {
                            firstCell = null;
                        }
                    }
                } else {
                    secondCell = (mxCell) mxGraphOutline.getGraphComponent().getCellAt(e.getX(), e.getY());
                    System.out.println();
                    if (secondCell == null){
                        graph.getModel().beginUpdate(); // начали обновлять
                        try
                        {
                            firstCell.setStyle("defaultStyle;");
                            firstCell = null;
                            graph.refresh();
                        }
                        finally
                        {
                            graph.getModel().endUpdate(); // закончили
                        }
                    } else {
                        if (secondCell.isVertex()){
                            if (connectionVertex(firstCell, secondCell)){
                                graph.getModel().beginUpdate(); // начали обновлять
                                try
                                {
                                    firstCell.setStyle("defaultStyle;");
                                    firstCell = null;
                                    graph.refresh();
                                }
                                finally
                                {
                                    graph.getModel().endUpdate(); // закончили
                                }
                                JOptionPane.showMessageDialog(null,  "These vertices are already connected!", "Invalid selection of vertices", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JTextField edgeWeight = new JTextField();
                                final JComponent[] inputs = new JComponent[] {
                                        new JLabel("Enter weight (Positive integer)"),
                                        edgeWeight
                                };
                                int result = JOptionPane.showConfirmDialog(null, inputs, "Add edge", JOptionPane.PLAIN_MESSAGE);
                                if (result == JOptionPane.OK_OPTION) {
                                    Scanner sc = new Scanner(edgeWeight.getText().trim());
                                    if (!sc.hasNextInt()){
                                        graph.getModel().beginUpdate(); // начали обновлять
                                        try
                                        {
                                            firstCell.setStyle("defaultStyle;");
                                            firstCell = null;
                                            graph.refresh();
                                        }
                                        finally
                                        {
                                            graph.getModel().endUpdate(); // закончили
                                        }
                                        JOptionPane.showMessageDialog(null,  "You entered not an integer!", "Invalid weight input", JOptionPane.ERROR_MESSAGE);

                                    } else {
                                        int weight = sc.nextInt();
                                        if (sc.hasNext()){
                                            graph.getModel().beginUpdate(); // начали обновлять
                                            try
                                            {
                                                firstCell.setStyle("defaultStyle;");
                                                firstCell = null;
                                                graph.refresh();
                                            }
                                            finally
                                            {
                                                graph.getModel().endUpdate(); // закончили
                                            }
                                            JOptionPane.showMessageDialog(null,  "Wrong number of arguments!", "Invalid weight input", JOptionPane.ERROR_MESSAGE);
                                        } else if (weight < 0){
                                            graph.getModel().beginUpdate(); // начали обновлять
                                            try
                                            {
                                                firstCell.setStyle("defaultStyle;");
                                                firstCell = null;
                                                graph.refresh();
                                            }
                                            finally
                                            {
                                                graph.getModel().endUpdate(); // закончили
                                            }
                                            JOptionPane.showMessageDialog(null,  "The number must be positive!", "Invalid weight input", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            graph.getModel().beginUpdate(); // начали обновлять
                                            try
                                            {
                                                firstCell.setStyle("defaultStyle;");
                                                graph.insertEdge(parent, null, edgeWeight.getText(), firstCell, secondCell);
                                                new mxParallelEdgeLayout(graph).execute(parent);
                                                firstCell = null;
                                                secondCell = null;
                                                graph.refresh();
                                            }
                                            finally
                                            {
                                                graph.getModel().endUpdate(); // закончили
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }

            public void changeAction(){
                firstCell = null;
                secondCell = null;
            }
        };

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

        VertexEnumerator vertexEnumerator = new VertexEnumerator();
        MouseAdapter addVertexMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                graph.getModel().beginUpdate(); // начали обновлять
                try
                {
                    Object v2 = graph.insertVertex(parent, null, vertexEnumerator.getNextValue(), e.getX() - widthVertex / 2, e.getY() - heightVertex / 2, widthVertex, heightVertex);
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

        MouseAdapter renameVertexMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mxCell cell = (mxCell) mxGraphOutline.getGraphComponent().getCellAt(e.getX(), e.getY());
                if (cell != null){
                    if (cell.isVertex()){
                        JTextField changeName = new JTextField(cell.getValue().toString());
                        final JComponent[] inputs = new JComponent[] {
                                new JLabel("Enter new name"),
                                changeName
                        };
                        int result = JOptionPane.showConfirmDialog(null, inputs, "Change name", JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            graph.getModel().beginUpdate(); // начали обновлять
                            try
                            {
                                graph.getModel().setValue(cell, changeName.getText());
                                graph.refresh();
                            }
                            finally
                            {
                                graph.getModel().endUpdate(); // закончили
                            }
                        }
                    }
                }
            }
        };




        graphComponent.getGraphControl().addMouseListener(moveVertexAdapter);
        graphComponent.getGraphControl().addMouseMotionListener(moveVertexAdapter);



        JPanel panelCenter = new JPanel();
        JPanel panelRightRight = new JPanel();
        JPanel panelRightRightDown = new JPanel();
        panelRightRight.setMinimumSize(new Dimension(700, 30));

        BoxLayout boxlayoutVerticalAll = new BoxLayout(panelCenter, BoxLayout.Y_AXIS);
        BoxLayout boxlayoutAll = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
        BoxLayout boxlayoutRightRight = new BoxLayout(panelRightRight, BoxLayout.X_AXIS);
        BoxLayout boxlayoutRightRightDown = new BoxLayout(panelRightRightDown, BoxLayout.X_AXIS);

        panelCenter.setLayout(boxlayoutVerticalAll);
        panelRightRight.setLayout(boxlayoutRightRight);
        panelRightRightDown.setLayout(boxlayoutRightRightDown);
        getContentPane().setLayout(boxlayoutAll);

        JButton jButtonNewGraph = new JButton("New_Graph");
        JToggleButton jButtonAddVertex = new JToggleButton("Add Vertex");
        JToggleButton jButtonAddEdge = new JToggleButton("Add Edge");
        JToggleButton jButtonRename = new JToggleButton("Rename");
        JToggleButton jButtonRemove = new JToggleButton("Remove");


        JButton jButtonStepUndo = new JButton("Play back");
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





        // panelRightRight.add(jButtonNewGraph);
        //panelRightRight.add(jButtonLoad);
        panelRightRight.add(jButtonAddVertex);
        panelRightRight.add(jButtonAddEdge);
        panelRightRight.add(jButtonRename);
        panelRightRight.add(jButtonRemove);


        panelRightRightDown.add(jButtonStepUndo);
        panelRightRightDown.add(jButtonStep);
        panelRightRightDown.add(jButtonAll);


        panelCenter.add(panelRightRight);
        panelCenter.add(jTextMatrixSmeshznosti);
        panelCenter.add(jFieldTable);
        panelCenter.add(panelRightRightDown);


        jButtonAddVertex.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(itemEvent.getStateChange()==ItemEvent.SELECTED){
                    jButtonRemove.setSelected(false);
                    jButtonAddEdge.setSelected(false);
                    jButtonRename.setSelected(false);
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
                    jButtonAddVertex.setSelected(false);
                    jButtonAddEdge.setSelected(false);
                    jButtonRename.setSelected(false);
                    graphComponent.getGraphControl().addMouseListener(deleteCellMouseAdapter);
                } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED){
                    graphComponent.getGraphControl().removeMouseListener(deleteCellMouseAdapter);
                }
            }
        });

        jButtonAddEdge.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange()==ItemEvent.SELECTED){
                    jButtonAddVertex.setSelected(false);
                    jButtonRemove.setSelected(false);
                    jButtonRename.setSelected(false);
                    graphComponent.getGraphControl().addMouseListener(addEdgeMouseAdapter);
                } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED){
                    graphComponent.getGraphControl().removeMouseListener(addEdgeMouseAdapter);
                }
            }
        });

        jButtonRename.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange()==ItemEvent.SELECTED){
                    jButtonAddVertex.setSelected(false);
                    jButtonRemove.setSelected(false);
                    jButtonAddEdge.setSelected(false);
                    graphComponent.getGraphControl().addMouseListener(renameVertexMouseAdapter);
                } else if(itemEvent.getStateChange()==ItemEvent.DESELECTED){
                    graphComponent.getGraphControl().removeMouseListener(renameVertexMouseAdapter);
                }
            }
        });


        getContentPane().add(graphComponent);
        getContentPane().add(panelCenter);
    }




    static JFrame getFrame(){
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 2 - 375, dimension.height / 2 - 250, 750, 500);
        frame.setVisible(true);
        return frame;
    }

    public static void main(String[] args)
    {
        jFrame = getFrame();
    }

}