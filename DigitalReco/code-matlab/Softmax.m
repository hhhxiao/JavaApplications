function y = Softmax(x)
    m = max(x);
    x = x-m*ones(size(x));
  ex = exp(x);
  y  = ex / sum(ex);
end