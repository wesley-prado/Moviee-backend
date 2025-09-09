CREATE
EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE sessions
  ADD CONSTRAINT sessions_room_time_overlap_excl EXCLUDE USING GIST (
    room_id WITH =,
    tstzrange(start_time, end_time) WITH &&
);