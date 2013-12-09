package org.hoteia.qalingo.core.pojo.util.mapper;

import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PojoUtil {
    
    public static <T> List<T> asList(Collection<T> collection) {
        if(collection != null){
            return new ArrayList<T>(collection);
        }
        return null;
    }

    public static <T> List<T> mapAll(Mapper mapper, Collection<?> sources, Class<T> outputType) {
        List<T> result = new ArrayList<T>(sources.size());
        for(Object source: sources) {
            result.add(mapper.map(source, outputType));
        }
        return result;
    }

}