%
%|img|------filters------->|cov_maps|-----Pool()----->|pool_maps|----resharp()---->|pool_layer|---W_1-->|hide_layer|---W_2---|output_layer|
%28*28          
%
function BatchTrain(n)
%���ز�������
Data  = load('MNISTData.mat');
imgs = Data.X_Train;
Ds = Data.D_Train;

%��ʼ���������
batch_num = n;
main_num = 60000/batch_num;
filter_num = 10; %�˲�������
filter_width = 9;%�˲������
alpha = 0.01;%Ȩֵϵ��
hide_layer_height = 70; %������߶�

%�����������
cov_maps_width = 29-filter_width;%����ͼ�Ŀ��
pool_maps_width = cov_maps_width/2;%�ػ�������ͼ�Ŀ��

pool_layer_height = pool_maps_width*pool_maps_width*filter_num;%�ػ�����߶�

%��ʼ������ϵ������
filters = randn(filter_width,   filter_width,   filter_num);     %��ʼ�˲�����(9*9*20)
W_1 = (2*rand(hide_layer_height,    pool_layer_height)-1)/20;      % ��ʼ���������������֮���ϵ��(100*2000)
W_2 = (2*rand(10,   hide_layer_height)-1)/10;   % ��ʼ���������������֮���ϵ��(10*100)
%��ѭ��

for i = 1:main_num
        DW_f = zeros(size(filters));
        DW_1 = zeros(size(W_1));
        DW_2 = zeros(size(W_2));
        for n = 1:batch_num
                num = (i-1)*batch_num+n;
                img = imgs(:,:,num);      %����ͼƬ(28*28)
                d = Ds(:,num,:);     %img ��Ӧ������  
                cov_maps_x = Conv(img,filters);       %����������ͼ��
                cov_maps_y = ActiveFun(cov_maps_x);   %���������������ͼ��

                %�ػ�
                pool_maps = Pool(cov_maps_y);      %�����ػ���ĳػ�ͼ��()
                pool_layer = reshape(pool_maps, [], 1);     %�ػ�ͼ�����ת��Ϊxxx*1����������Ϊѵ�����������

                %������
                hide_layer_v = W_1*pool_layer;       %�������
                hide_layer_y = ActiveFun(hide_layer_v);         %�������y y = psi(v)Ҳ���Ǽ��������  

                %�����
                output_v = W_2*hide_layer_y;%������v
                output_y= Softmax(output_v);%������y

               %������bp
                e_output = d - output_y; %��������
                delta_output = e_output;      %�����deltaֵ

                e_hide = W_2'*delta_output;    %���������
                delta_hide = (hide_layer_v>0).*e_hide; %������deltaֵ

                e_pool = W_1'*delta_hide;%�ػ������

                %��������Ȩֵϵ������
                dw_2 = alpha*delta_output*hide_layer_y';
                dw_1 = alpha*delta_hide*pool_layer';
                %����W1��W0
                DW_1 = DW_1+dw_1;
                DW_2= DW_2+dw_2;

                %������ȡ������
                e_cov = reshape(e_pool,size(pool_maps));
                E = zeros(size(cov_maps_y));
                e_cov= e_cov/4;
                E(1:2:end,1:2:end,:) = e_cov;
                E(1:2:end,2:2:end,:) = e_cov;
                E(2:2:end,1:2:end,:) = e_cov;
                E(2:2:end,2:2:end,:) = e_cov;

                delta_cov = (cov_maps_x>0).*E;
                d_filters = alpha*Conv(img,delta_cov);
                DW_f = DW_f+d_filters;
        end
        W_1 = W_1+DW_1/batch_num;
        W_2 = W_2+DW_2/batch_num;
        filters = filters+DW_f/batch_num;
end
%��������
save ('result.mat','filters','W_1','W_2');
end

