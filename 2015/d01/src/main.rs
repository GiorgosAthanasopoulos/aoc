use std::fs::File;
use std::env;
use std::io::{BufReader, BufRead};

fn main() -> Result<(), &'static str> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename")
    }
    let path = args[1].clone();
    let file = File::open(path).unwrap();
    let reader = BufReader::new(file);

    let mut floor = 0;
    let mut position = 0;
    for line in reader.lines() {
        for (i, char) in line.unwrap().chars().enumerate() {
            if char == '(' {
                floor += 1;
            } else {
                floor -= 1;
            }
            if floor < 0 && position == 0 {
                position = i + 1;
            }
        }
    }

    println!("{position}");
    Ok(())
}
