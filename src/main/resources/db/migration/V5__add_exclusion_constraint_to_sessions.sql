CREATE
    EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE cinema.sessions
    DROP CONSTRAINT IF EXISTS sessions_room_time_overlap_excl;

ALTER TABLE cinema.sessions
    ADD CONSTRAINT sessions_room_time_overlap_excl EXCLUDE USING GIST (
        room_id WITH =,
        tstzrange(start_time, end_time) WITH &&
        );
