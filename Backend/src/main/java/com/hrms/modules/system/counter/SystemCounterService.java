package com.hrms.modules.system.counter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemCounterService {

    @Autowired
    SystemCounterRepository repository;


    public String getNextFormattedValue(String counterCode) {

        String formattedNumber = "-9999999999";

        SystemCounter counterInst = this.repository.findByCode(counterCode);
        if (counterInst != null) {

            String prefix = counterInst.prefix;
            Long nextNumber = counterInst.nextNumber;
            Integer step = counterInst.step;
            Integer counterWidth = counterInst.counterWidth;
            String prefixSeparator = counterInst.prefixSeparator;
            String suffix = counterInst.suffix;
            String suffixSeparator = counterInst.suffixSeparator;
            String numerationType = counterInst.numerationType;

            if (prefixSeparator == null) prefixSeparator = "";
            if (suffixSeparator == null) suffixSeparator = "";


            // update counter value
            counterInst.nextNumber = nextNumber + step;
            this.repository.saveAndFlush(counterInst);


            int nextNumberLength = nextNumber.toString().length();
            if (prefix != null) {
                Integer prefixLength = prefix.length();
                if (prefixLength > 0) {
                    counterWidth = counterWidth - prefixLength - 1;
                }
            }


            if(numerationType.equals("1")){        // full Numeric

                return formattedNumber;

            } else if(numerationType.equals("2")){ // Alpha Numeric

                if(nextNumberLength < counterWidth){
                    // Need to add 0 in left
                    formattedNumber = StringUtils.leftPad(nextNumber.toString(), counterWidth, "0");
                } else {
                    formattedNumber = nextNumber.toString();
                }

                if( prefix == null || prefix.equals("") ) prefix = "";
                if( suffix == null || suffix.equals("") ) suffix = "";
                return prefix + prefixSeparator + formattedNumber + suffixSeparator + suffix;

            }

            return formattedNumber;

        }

        return null;

    }





}
