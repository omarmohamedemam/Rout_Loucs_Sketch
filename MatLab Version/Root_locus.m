
function  Root_locus
    num=[0,0,0,0,1];
    den=[1,125,5100,6500,0];
    kend=1000;
    usrtitle='Rout Locus graph --- by: Omar Emam';
    TF=tf(num,den);
    disp('Transfer Function: ');
    TF
    locus = zeros(kend, length(den)-1);
    newnum = [zeros(1,length(den)-length(num)) num];
    kdata=0:kend; % Range of values for the gain K
    for K=kdata % K is the Gain
        locus(K+1,:)=roots(den+K*newnum)';
    end
    
    %% Poles and zeros of the open loop
    if length(den>1)
        op = roots(den); % Open loop poles
        disp('Poles: ');
        disp(op);
        opRe = real(op); % Real part
        opIm = imag(op); % Imaginary part
    end
    if length(num>1)
        oz = roots(num); % Open loop zeros
        disp('Zeros: ');
        disp(oz);
        ozRe = real(oz); % Real part
        ozIm = imag(oz); % Imaginary part
    end
    clf; % Clear figure
    plot(opRe, opIm,'x','LineWidth', 2, 'MarkerSize',15); hold;
    plot(ozRe, ozIm,'o','LineWidth', 2, 'MarkerSize',8);
    plot(locus); % Plot the root loci
   
    sgrid; % Display the special grid Radial lines indicate zeta and circles indicate w_n
    title(usrtitle);
    xlabel('Real Axis')
    ylabel('Imaginary Axis')
    axis equal % Scale same for both the axes
    hold off
end