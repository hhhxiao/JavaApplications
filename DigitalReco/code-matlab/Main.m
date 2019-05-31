Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;
alpha = 0.01;
filters = randn(9,9,20);     %��ʼ�˲�����(9*9*20)
W_1 = (2*rand(100,2000)-1)/20;      % ��ʼ���������������֮���ϵ��(100*2000)
W_2 = (2*rand(10,100)-1)/10;   % ��ʼ���������������֮���ϵ��(10*100)
for n = 1:60000
        img = imgs(:,:,n);      %���ͼƬ(28*28)
        d = Ds(:,n,:);     %img ��Ӧ������  
        cov_maps = Conv(img,filters);       %����������ͼ��(20*20*20)
        active_cov_maps = ActiveFun(cov_maps);   %���������������ͼ��(20*20*20)
        pool_maps = Pool(active_cov_maps);      %�����ػ���ĳػ�ͼ��()(10*10*20)
        pool_array = reshape(pool_maps, [], 1);     %�ػ�ͼ�����ת��Ϊ2000*1��������

        hide_layer_v = W_1*pool_array;       %�������v(100*1)
        hide_layer_y = ActiveFun(hide_layer_v);         %�������y y = psi(v)Ҳ���Ǽ��������  100*1
       
        output_v = W_2*hide_layer_y;%������v
        output = Softmax(output_v);%������y
       
       %������bp
        e_output = d - output; %��������
        delta_output = e_output;      %�����deltaֵ
        e_hide = W_2'*delta_output;    %���������
        delta3 = (hide_layer_v>0).*e_hide;
        e2 = W_1'*delta3;
        dw_2 = alpha*delta_output*hide_layer_y';
        dw_1 = alpha*delta3*pool_array';
        %����W1��W2
        W_1 = W_1+dw_1;
        W_2 = W_2+dw_2;
        %������ȡ������
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













