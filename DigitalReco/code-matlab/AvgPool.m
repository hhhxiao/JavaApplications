function y = AvgPool(x)
% ����һ��2m*2m*p������ͼ��
% ���һ��m*m*p�ĳػ�ͼ��
% 2x2 mean pooling
%2*2 ƽ���ػ�
%
y=(x(1:2:end,1:2:end,:)+x(2:2:end,1:2:end,:)+x(1:2:end,2:2:end,:)+x(2:2:end,2:2:end,:))/4;
end
 