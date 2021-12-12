package hello.core.discount;

import hello.core.member.Member;

public interface DiscountPolicy {
    
    /**
     * @return  할인 대상 금액.
     *  
     * @author juhan
     * @since 2021-07-21 
    **/
    
    int discount(Member member, int price);
}
