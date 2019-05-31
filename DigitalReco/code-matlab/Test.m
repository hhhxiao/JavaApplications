time=zeros(2,20);
for i=1:20
tic;
BatchTrain(i*10);
time(1,i)=toc;
time(2,i) = showAccuracy();
end
mean(time)