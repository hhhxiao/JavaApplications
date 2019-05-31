function NoConv()
tic
%
%|img|-----Pool()----->|pool_maps|----resharp()---->|pool_layer|---W_1-->|hide_layer|---W_2---|output_layer|
%28*28          
%
%计时器
%加载测试数据
Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;

%初始化
alpha = 0.01;
W_1 = (2*rand(400,196)-1)/20;      % 初始化输入层与隐含层之间的系数(100*2000)
W_15 = (2*rand(50,400)-1);
W_2 = (2*rand(10,50)-1)/10;   % 初始化隐含层与输出层之间的系数(10*100)

%主循环
for n = 1:60000
    
        %加载图片
        img = imgs(:,:,n);      %加载图片(28*28)
        d = Ds(:,n,:);     %img 对应的期望  
    
        pool_array = AvgPool(img);
        input = reshape(pool_array, [], 1);     %784*1
        
        %隐含层1
        hide_layer_v = W_1*input;       %500*784  x  784*1
        hide_layer_y = ActiveFun(hide_layer_v);         %500*1 
       
        %隐含层2
        hide_layer_v_2 = W_15*hide_layer_y;   %200*500   x 500*1
        hide_layer_y_2 = ActiveFun(hide_layer_v_2);  %200*1
        
        %输出层
        output_v = W_2*hide_layer_y_2;% 10*200 *200*1
        output_y= Softmax(output_v);%10*1
       
        
        
       %下面是bp算法
        e_output = d - output_y; %输出层误差）（10*1）
        delta_output = e_output;      %输出层delta值   (10*1)
        
        e_hide = W_2'*delta_output;    %隐含层误差  (200*10x 10*1)200*1
        delta_hide = (hide_layer_v_2>0).*e_hide; %隐含层delta值(200*1  .x  100*1  === 200*1)
        
        
        e_hide_2 = W_15'*delta_hide;%隐含层2的误差500*200 X 200*1 == = 500*1
        delta_hide_2 = (hide_layer_v>0).*e_hide_2; % 500*1
        
        
        
        
        %纠正权值系数矩阵
        dw_2 = alpha*delta_output*hide_layer_y_2';%(   10*1x1*200)
        
        dw_1 = alpha*delta_hide_2*input';%(500x 1  * 1x784)
  
        dw_15 = alpha*delta_hide*hide_layer_y'; %(200 * 1 x 1*500)
      
        %更新W1和W0
       W_2 = W_2 + dw_2;
       W_15 = W_15 + dw_15;
      W_1 = W_1+dw_1;
      
end
toc
%存下数据
save ('result.mat','W_1','W_2','W_15');
disp('finished');
end
