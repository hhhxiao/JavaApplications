function Train()
%
%|img|------filters------->|cov_maps|-----Pool()----->|pool_maps|----resharp()---->|pool_layer|---W_1-->|hide_layer|---W_2---|output_layer|
%28*28          
%
%计时器
%加载测试数据
Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;

%初始化各项参数
filter_num = 10; %滤波器数量
filter_width = 9;%滤波器宽度
alpha = 0.01;%权值系数
hide_layer_height = 70; %隐含层高度
tic
%各项二级参数
cov_maps_width = 29-filter_width;%特征图的宽度
pool_maps_width = cov_maps_width/2;%池化后特征图的宽度

pool_layer_height = pool_maps_width*pool_maps_width*filter_num;%池化数组高度

%初始化三个系数矩阵
filters = randn(filter_width,   filter_width,   filter_num);     %初始滤波器组(9*9*20)
W_1 = (2*rand(hide_layer_height,    pool_layer_height)-1)/20;      % 初始化输入层与隐含层之间的系数(100*2000)
W_2 = (2*rand(10,   hide_layer_height)-1)/10;   % 初始化隐含层与输出层之间的系数(10*100)
%主循环
for n = 1:60000
        img = imgs(:,:,n);      %加载图片(28*28)
        d = Ds(:,n,:);     %img 对应的期望  
        cov_maps_x = Conv(img,filters);       %卷积后的特征图组
        cov_maps_y = ActiveFun(cov_maps_x);   %经过激活函数的特征图组
        
        %池化
        pool_maps = Pool(cov_maps_y);      %经过池化后的池化图组()
        pool_layer = reshape(pool_maps, [], 1);     %池化图组矩阵转化为xxx*1的列向量作为训练网络的输入

        %隐含层
        hide_layer_v = W_1*pool_layer;       %隐含层的
        hide_layer_y = ActiveFun(hide_layer_v);         %隐含层的y y = psi(v)也就是激活函数处理  
       
        %输出层
        output_v = W_2*hide_layer_y;%输出层的v
        output_y= Softmax(output_v);%输出层的y
       
       %下面是bp
        e_output = d - output_y; %输出层误差
        delta_output = e_output;      %输出层delta值
        
        e_hide = W_2'*delta_output;    %隐含层误差
        delta_hide = (hide_layer_v>0).*e_hide; %隐含层delta值
        
        e_pool = W_1'*delta_hide;%池化层误差
        
        %纠正两个权值系数矩阵
        dw_2 = alpha*delta_output*hide_layer_y';
        dw_1 = alpha*delta_hide*pool_layer';
        %更新W1和W0
        W_1 = W_1+dw_1;
        W_2 = W_2+dw_2;
        
        %特征提取层误差传递
        e_cov = reshape(e_pool,size(pool_maps));
        E = zeros(size(cov_maps_y));
        e_cov= e_cov/4;
        E(1:2:end,1:2:end,:) = e_cov;
        E(1:2:end,2:2:end,:) = e_cov;
        E(2:2:end,1:2:end,:) = e_cov;
        E(2:2:end,2:2:end,:) = e_cov;
        
        delta_cov = (cov_maps_x>0).*E;
        d_filters = alpha*Conv(img,delta_cov);
        filters = filters+d_filters;
end
toc
%存下数据
save ('result.mat','filters','W_1','W_2');
disp('finished');
end
