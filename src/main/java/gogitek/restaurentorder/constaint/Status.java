package gogitek.restaurentorder.constaint;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Status {
    PROCESSING("PROCESSING", 0),DONE("DONE", 3), APPROVED("APPROVED", 1), CANCELED("CANCELED", 2), DELIVERED("DELIVERED", 4), UNKNOWN("UNKNOWN", -1);
    private final String text;
    private final Integer value;

    private static final Map<Integer, Status> CONFLICT_STATUS_MAP = new HashMap<>();

    private static final Map<String, Status> CONFLICT_STATUS_TEXT_MAP = new HashMap<>();

    static {
        for (Status status : Status.values()) {
            CONFLICT_STATUS_MAP.put(status.value, status);
            CONFLICT_STATUS_TEXT_MAP.put(status.text, status);
        }
    }

    @JsonCreator
    public static Status fromValue(Integer value) {
        if (value == null) {
            return Status.UNKNOWN;
        }
        Status conflictStatus = CONFLICT_STATUS_MAP.get(value);
        if (conflictStatus == null) {
            return Status.UNKNOWN;
        }
        return conflictStatus;
    }

    public static Status fromTextValue(String value) {
        if (value == null) {
            return Status.UNKNOWN;
        }
        Status conflictStatus = CONFLICT_STATUS_TEXT_MAP.get(value);
        if (conflictStatus == null) {
            return Status.UNKNOWN;
        }
        return conflictStatus;
    }

    @Converter(autoApply = true)
    public static class ConflictStatusConverter implements
            AttributeConverter<Status, Integer> {

        @Override
        public Integer convertToDatabaseColumn(Status transType) {
            if (transType == null) {
                return null;
            }
            return transType.getValue();
        }

        @Override
        public Status convertToEntityAttribute(Integer value) {
            return Status.fromValue(value);
        }

    }


}
