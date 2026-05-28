CREATE TABLE IF NOT EXISTS luxury_watches (
    id SERIAL PRIMARY KEY,

    "Brand" VARCHAR(100),
    "Model" VARCHAR(100),

    "Case Material" VARCHAR(100),
    "Strap Material" VARCHAR(100),
    "Movement Type" VARCHAR(100),

    "Water Resistance (meters)" SMALLINT,

    "Case Diameter (mm)" NUMERIC(5,2),
    "Case Thickness (mm)" NUMERIC(5,2),
    "Band Width (mm)" NUMERIC(5,2),

    "Dial Color" VARCHAR(50),
    "Crystal Material" VARCHAR(100),
    "Complications" VARCHAR(255),

    "Power Reserve (hours)" SMALLINT,

    "Price (USD)" INTEGER
);

INSERT INTO luxury_watches (
    "Brand",
    "Model",
    "Case Material",
    "Strap Material",
    "Movement Type",
    "Water Resistance (meters)",
    "Case Diameter (mm)",
    "Case Thickness (mm)",
    "Band Width (mm)",
    "Dial Color",
    "Crystal Material",
    "Complications",
    "Power Reserve (hours)",
    "Price (USD)"
)
VALUES (
    'Rolex',
    'Submariner',
    'Steel',
    'Oystersteel',
    'Automatic',
    300,
    41.00,
    12.50,
    20.00,
    'Black',
    'Sapphire',
    'Date',
    70,
    12000
),
(
    'Omega',
    'Seamaster',
    'Steel',
    'Rubber',
    'Automatic',
    300,
    42.00,
    13.00,
    20.00,
    'Blue',
    'Sapphire',
    'Helium Valve',
    55,
    8000
);