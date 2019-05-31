import java.awt.*;
import javax.swing.*;

public class AlgoFrame extends JFrame{

    private int canvasWidth;
    private int canvasHeight;
    private int  M;
    private int N;
    private int[][] grids;
    private int score;
    private int dificulty;

    public AlgoFrame(String title, int canvasWidth, int canvasHeight){

        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        AlgoCanvas canvas = new AlgoCanvas();
        setContentPane(canvas);
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    private int getCanvasWidth(){return canvasWidth;}

    private int getCanvasHeight(){return canvasHeight;}

    // TODO: 设置自己的数据
    void render(int[][] grids, int M, int N,int score,int dificulty){
        this.grids = grids;
        this.M = M;
        this.N = N;
        this.score = score;
        this.dificulty = dificulty;
        repaint();//clear the screen and paint again
    }

    private class AlgoCanvas extends JPanel{

        AlgoCanvas(){
            // 双缓存
            super(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D)g;

            // 抗锯齿
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.addRenderingHints(hints);


            // 具体绘制
            int w = canvasWidth/M;
            AlgoVisHelper.setStrokeWidth(g2d,3);
            AlgoVisHelper.setColor(g2d,AlgoVisHelper.Blue);
            AlgoVisHelper.strokeRectangle(g2d,0,0,getCanvasWidth(),w*N);
            AlgoVisHelper.strokeRectangle(g2d,0,w*N+10,getCanvasWidth(),getCanvasHeight()-1-w*N);
            AlgoVisHelper.setColor(g2d,AlgoVisHelper.Black);
            Font f = new Font("微软雅黑",Font.BOLD,30);
            g2d.setFont(f);
            AlgoVisHelper.drawText(g2d,"score:"+score+"       level:"+dificulty,200,w*N+(getCanvasHeight()-w*N)/2);


            //int h = canvasHeight/N;
            for(int i = 0;i<M;i++)
            {
                for(int j = 0;j< N;j++)
                {
                    if(grids[i][j] == 1)
                    {
                        AlgoVisHelper.setColor(g2d,AlgoVisHelper.Orange);
                        AlgoVisHelper.fillRectangle(g2d,w*i+1,w*j+1,w-1,w-1);
                    }
                }
            }
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}


