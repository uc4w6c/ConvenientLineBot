CREATE table tasks(id SERIAL primary key, source_type integer, user_id varchar(50), group_id varchar(50), room_id varchar(50), todo_text varchar(150), created_at time, created_at time);
CREATE table states(id SERIAL primary key, source_type integer, user_id varchar(50), group_id varchar(50), room_id varchar(50), state_kind integer, status integer, updated_at time);
