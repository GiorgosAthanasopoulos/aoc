use std::{env, fs::File, io::{BufReader, BufRead}};
use md5;

fn main() -> Result<(), &'static str> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename")
    }
    let filename = args[1].clone();
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    for line in reader.lines() {
        let line_copy = line.unwrap();
        let mut number = 100000;
        loop {
            let food_string = format!("{line_copy}{number}");
            let hash = format!("{:?}", md5::compute(food_string.as_bytes()));
            if &hash[0..6] == "000000" {
                break
            }
            number += 1
        }
        println!("{number}")
    }

    Ok(())
}
