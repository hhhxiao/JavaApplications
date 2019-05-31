function y = AvgPool(x)
% 输入一个2m*2m*p的特征图组
% 输出一个m*m*p的池化图组
% 2x2 mean pooling
%2*2 平均池化
%
y=(x(1:2:end,1:2:end,:)+x(2:2:end,1:2:end,:)+x(1:2:end,2:2:end,:)+x(2:2:end,2:2:end,:))/4;
end
 