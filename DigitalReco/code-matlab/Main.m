Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;
alpha = 0.01;
filters = randn(9,9,20);     %初始滤波器组(9*9*20)
W_1 = (2*rand(100,2000)-1)/20;      % 初始化输入层与隐含层之间的系数(100*2000)
W_2 = (2*rand(10,100)-1)/10;   % 初始化隐含层与输出层之间的系数(10*100)
for n = 1:60000
        img = imgs(:,:,n);      %随机图片(28*28)
        d = Ds(:,n,:);     %img 对应的期望  
        cov_maps = Conv(img,filters);       %卷积后的特征图组(20*20*20)
        active_cov_maps = ActiveFun(cov_maps);   %经过激活函数的特征图组(20*20*20)
        pool_maps = Pool(active_cov_maps);      %经过池化后的池化图组()(10*10*20)
        pool_array = reshape(pool_maps, [], 1);     %池化图组矩阵转化为2000*1的列向量

        hide_layer_v = W_1*pool_array;       %隐含层的v(100*1)
        hide_layer_y = ActiveFun(hide_layer_v);         %隐含层的y y = psi(v)也就是激活函数处理  100*1
       
        output_v = W_2*hide_layer_y;%输出层的v
        output = Softmax(output_v);%输出层的y
       
       %下面是bp
        e_output = d - output; %输出层误差
        delta_output = e_output;      %输出层delta值
        e_hide = W_2'*delta_output;    %隐含层误差
        delta3 = (hide_layer_v>0).*e_hide;
        e2 = W_1'*delta3;
        dw_2 = alpha*delta_output*hide_layer_y';
        dw_1 = alpha*delta3*pool_array';
        %更新W1和W2
        W_1 = W_1+dw_1;
        W_2 = W_2+dw_2;
        %特征提取层误差传递
        E2 = reshape(e2,size(pool_maps));
        E1 = zeros(size(active_cov_maps)); E2_4= E2/4;
        E1(1:2:end,1:2:end,:) = E2_4;
        E1(1:2:end,2:2:end,:) = E2_4;
        E1(2:2:end,1:2:end,:) = E2_4;
        E1(2:2:end,2:2:end,:) = E2_4;
        delta1 = (cov_maps>0).*E1;
        d_filters = alpha*Conv(img,delta1);
        filters = filters+d_filters;
         
end
save ('result.mat','filters','W_1','W_2');













