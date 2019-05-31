import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.Deque;
import java.util.LinkedList;


public class AlgoVisualizer {

    // TODO: 创建自己的数据
    private AlgoFrame frame;    // 视图
    private  int[][] grids;     //储存棋盘信息
    private int M;      //棋盘长度
    private  int N;     //棋盘高度
    private Deque<Point> snake;     //储存蛇的双向队列
    private  enum dir{E,S,W,N,PAUSE} //方向信息
    private dir direction; //方向
    private int score;
    private boolean gameFlag;
    private int difficulty;
    private int pauseTime;


    private AlgoVisualizer(int sceneWidth, int sceneHeight){//constructor
        init();//data init();
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("Snack", sceneWidth, sceneHeight);
            // TODO: 根据情况决定是否加入键盘鼠标事件监听器
            frame.addKeyListener(new AlgoKeyListener());
            frame.addMouseListener(new AlgoMouseListener());
            new Thread(this::run).start();
        });
    }
    // 动画逻辑
    private void run(){

        while (true)
        {
            frame.render(grids,M,N,score,difficulty);//draw
            if(gameFlag)
            this.update();//update data
            //render all the time
            AlgoVisHelper.pause(pauseTime);
        }
    }

    private void move(int dx,int dy)
    {
        Point head_point = snake.remove();//获取并去除队头
        grids[head_point.getX()][head_point.getY()] = 0;//更新队头数据
        Point tail_point = snake.peekLast();//获取队尾
        Point next_point = new Point(tail_point.getX()+dx,tail_point.getY()+dy);//获取前方点的坐标
        if(grids[next_point.getX()][next_point.getY()] == 0){//前方点坐标是空的，没食物
            snake.add(next_point);//把前方点加入蛇身
            grids[next_point.getX()][next_point.getY()] = 1;//更新位置矩阵
    }else if(grids[next_point.getX()][next_point.getY()] == 1){//前方点坐标不是空的，有食物

            boolean flag = false;
            for(Point _point:snake)
            {
                if(_point.getX() == next_point.getX()&&_point.getY()==next_point.getY())
                {
                    flag = true;
                    System.out.println("game over");
                    for(int i = 0;i<M;i++)
                    {
                        for(int j = 0;j<N;j++)
                        {
                            grids[i][j] = 1;
                        }
                    }
                    gameFlag = false;


                }
            }
            if(!flag)
            {
                snake.add(next_point);//把前方点加入蛇身
                snake.add(new Point(next_point.getX()+dx,next_point.getY()+dy));//把非空白点加入
                grids[next_point.getX() +dx][next_point.getY()+dy] = 1;//更新矩阵
                generateFood();
                score++;
            }

        }

    }

    private void generateFood() {
        int a = 0;
        int x,y;

        do{
            x = (int) Math.floor(Math.random() * (M - 2)) + 1;
            y = (int) Math.floor(Math.random() * (N - 2)) + 1;
            for (Point p : snake) {
                if (p.getY() == y && p.getX() == x) {
                    a++;
                }
            }
        }while (a == 1);
        grids[x][y] = 1;

    }

    private void update()
    {

        try {
            switch (direction)
            {
                case W:
                    move(-1,0);
                    break;
                case N:
                    move(0,-1);
                    break;
                case S:
                    move(0,1);
                    break;
                case E:
                    move(1,0);
                    break;
                case PAUSE:
                    break;
                default:break;
            }
        }catch (ArrayIndexOutOfBoundsException e)
        {
            for(int i = 0;i<M;i++)
            {
                for(int j = 0;j<N;j++)
                {
                    grids[i][j] = 1;
                }
            }
            gameFlag = false;
        }

    }
    private void init()
    {
        snake = new LinkedList<>();
        direction = dir.N;
        M = 20;
        N = 30;

        gameFlag = true;
        difficulty = 3;
        pauseTime = 600-100*difficulty;
        score = 0;
        grids = new int[M][N];
        for(int i = 0;i<M;i++)
        {
            for(int j = 0;j<N;j++)
            {
                grids[i][j] = 0;
            }
        }
        //grids[(int)(M*Math.random())][(int)(N*Math.random())] = 1;
        snake.add(new Point(5,10));
        snake.add(new Point(5,11));
        grids[5][10] = 1;
        grids[5][11] = 1;
        grids[3][10] = 1;
    }



    //key listener
    private class AlgoKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyChar())
            {
                case 'w':
                    if(direction != dir.S)
                    direction = dir.N;break;
                case 'a':
                    if(direction != dir.E)
                    direction = dir.W;break;
                case 's':
                    if(direction != dir.N)
                    direction = dir.S;break;
                case 'd':
                    if(direction != dir.W)
                    direction = dir.E;break;
                case ' ':
                    gameFlag = !gameFlag;break;
                case '=':
                    if(difficulty<5)
                    {
                        difficulty+=1;
                        pauseTime = 600-100*difficulty;
                    }

                    System.out.println(difficulty);break;
                case '-':
                    if (difficulty>0)
                    {
                        difficulty-=1;
                        pauseTime = 600-100*difficulty;
                    }

                    System.out.println(difficulty);break;
                default:break;
            }
        }
    }
    private class AlgoMouseListener extends MouseAdapter{ }

    public static void main(String[] args) {

        int sceneWidth = 400;
        int sceneHeight = 700;

        // TODO: 根据需要设置其他参数，初始化visualizer
        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight);
    }
}
