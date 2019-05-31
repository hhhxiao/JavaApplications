function NoConv()
tic
%
%|img|-----Pool()----->|pool_maps|----resharp()---->|pool_layer|---W_1-->|hide_layer|---W_2---|output_layer|
%28*28          
%
%��ʱ��
%���ز�������
Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;

%��ʼ��
alpha = 0.01;
W_1 = (2*rand(400,196)-1)/20;      % ��ʼ���������������֮���ϵ��(100*2000)
W_15 = (2*rand(50,400)-1);
W_2 = (2*rand(10,50)-1)/10;   % ��ʼ���������������֮���ϵ��(10*100)

%��ѭ��
for n = 1:60000
    
        %����ͼƬ
        img = imgs(:,:,n);      %����ͼƬ(28*28)
        d = Ds(:,n,:);     %img ��Ӧ������  
    
        pool_array = AvgPool(img);
        input = reshape(pool_array, [], 1);     %784*1
        
        %������1
        hide_layer_v = W_1*input;       %500*784  x  784*1
        hide_layer_y = ActiveFun(hide_layer_v);         %500*1 
       
        %������2
        hide_layer_v_2 = W_15*hide_layer_y;   %200*500   x 500*1
        hide_layer_y_2 = ActiveFun(hide_layer_v_2);  %200*1
        
        %�����
        output_v = W_2*hide_layer_y_2;% 10*200 *200*1
        output_y= Softmax(output_v);%10*1
       
        
        
       %������bp�㷨
        e_output = d - output_y; %���������10*1��
        delta_output = e_output;      %�����deltaֵ   (10*1)
        
        e_hide = W_2'*delta_output;    %���������  (200*10x 10*1)200*1
        delta_hide = (hide_layer_v_2>0).*e_hide; %������deltaֵ(200*1  .x  100*1  === 200*1)
        
        
        e_hide_2 = W_15'*delta_hide;%������2�����500*200 X 200*1 == = 500*1
        delta_hide_2 = (hide_layer_v>0).*e_hide_2; % 500*1
        
        
        
        
        %����Ȩֵϵ������
        dw_2 = alpha*delta_output*hide_layer_y_2';%(   10*1x1*200)
        
        dw_1 = alpha*delta_hide_2*input';%(500x 1  * 1x784)
  
        dw_15 = alpha*delta_hide*hide_layer_y'; %(200 * 1 x 1*500)
      
        %����W1��W0
       W_2 = W_2 + dw_2;
       W_15 = W_15 + dw_15;
      W_1 = W_1+dw_1;
      
end
toc
%��������
save ('result.mat','W_1','W_2','W_15');
disp('finished');
end
