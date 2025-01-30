-- Define ENUM type for quest labels
CREATE TYPE quest_label_enum AS ENUM (
    'BOOK_KEEPING', 'GRAPHIC_DESIGN', 'HANDIWORK', 'ONLINE_TUTORIAL',
    'PET_SITTING', 'PHOTOGRAPHY', 'PICKUP_DELIVERY', 'SEWING'
);

-- Create message table
CREATE TABLE message (
    chat_session_id BIGINT NOT NULL,
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP(6),
    message TEXT,
    recipient TEXT,
    sender TEXT
);

-- Create quest_metadata table
CREATE TABLE quest_metadata (
    id BIGSERIAL PRIMARY KEY,
    quest_reward INTEGER,
    creation_timestamp TIMESTAMP(6),
    last_modified_timestamp TIMESTAMP(6),
    quest_creator_id BIGINT NOT NULL,
    quest_instructions TEXT NOT NULL,
    quest_validity TEXT NOT NULL,
    quest_label quest_label_enum
);

-- Create quest_session table
CREATE TABLE quest_session (
    id BIGSERIAL PRIMARY KEY,
    quest_acceptor_id BIGINT NOT NULL,
    quest_creator_id BIGINT,
    quest_id BIGINT NOT NULL,
    quest_status TEXT NOT NULL
);

-- Create user_information table
CREATE TABLE user_information (
    id BIGSERIAL PRIMARY KEY,
    email TEXT UNIQUE, -- Ensuring uniqueness for email
    first_name TEXT,
    last_name TEXT,
    picture TEXT
);

-- Add foreign key constraint to message table
ALTER TABLE message
ADD CONSTRAINT FK_message_chat_session
FOREIGN KEY (chat_session_id)
REFERENCES quest_session(id)
ON DELETE CASCADE;
