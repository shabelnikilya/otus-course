package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final List<Message> messageList = new ArrayList<>();


    @Override
    public void onUpdated(Message msg) {
        Message cloneMessage = msg.clone();
        messageList.add(cloneMessage);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messageList.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }
}
