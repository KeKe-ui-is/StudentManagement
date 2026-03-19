package raisetech.student.management.service;

import org.springframework.stereotype.Component;

@Component
public class Components {
    String createId(String currentMaxId,Character s){
        int number = Integer.parseInt(currentMaxId.substring(2));
        return String.format("%c-%03d",s,number + 1);
    }
}
