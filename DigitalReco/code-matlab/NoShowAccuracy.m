function acc =  NoShowAccuracy
clear all; close all;
load MNISTData X_Test D_Test
load result.mat
N = length(D_Test);
d_comp = zeros(1,N);
for k = 1:N
  X = X_Test(:, :, k); % Input,           28x28
  t = Pool(X);
  y2 = reshape(t, [], 1);          % 2000x1  
  v3 = W_1*y2;                       %  100x1
  y3 = ReLU(v3); 
  v4 = W_15*y3;
  y4 = ReLU(v4);
  v5  = W_2*y4;
  y  = Softmax(v5);                  %10x1

  [~, i] = max(y);
  d_comp(k) = i;
end
[~, d_true] = max(D_Test);
correctMsk = (d_comp == d_true);
acc = sum(correctMsk)/N
end
