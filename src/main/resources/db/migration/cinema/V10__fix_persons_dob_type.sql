ALTER TABLE IF EXISTS cinema.persons
    ALTER COLUMN dob TYPE DATE
        USING dob::DATE;
