use std::{
    env,
    fs::File,
    io::{BufRead, BufReader},
};

fn toggle(lights: &mut Vec<Vec<u8>>, start_x: i32, start_y: i32, end_x: i32, end_y: i32) {
    for (i, row) in lights.clone().iter().enumerate() {
        for (j, _) in row.iter().enumerate() {
            let i = i as i32;
            let j = j as i32;
            if i >= start_y && i <= end_y {
                if j >= start_x && j <= end_x {
                    let i = i as usize;
                    let j = j as usize;
                    lights[i][j] += 2;
                }
            }
        }
    }
}

fn turn(lights: &mut Vec<Vec<u8>>, start_x: i32, start_y: i32, end_x: i32, end_y: i32, toggle: u8) {
    for (i, row) in lights.clone().iter().enumerate() {
        for (j, _) in row.iter().enumerate() {
            let i = i as i32;
            let j = j as i32;
            if i >= start_y && i <= end_y {
                if j >= start_x && j <= end_x {
                    let i = i as usize;
                    let j = j as usize;
                    if toggle == 1 {
                        lights[i][j] += 1;
                    } else {
                        if lights[i][j] > 0 {
                            lights[i][j] -= 1;
                        }
                    }
                }
            }
        }
    }
}

fn main() -> Result<(), &'static str> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename");
    }
    let filename = &args[1];
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let mut lights = vec![vec![0u8; 1000]; 1000];
    for line in reader.lines() {
        let line = line.unwrap();
        let mut line_parts = line.split(" ");
        let first_word = line_parts.nth(0).unwrap();
        let second_word = line_parts.nth(0).unwrap();
        let third_word = line_parts.nth(0).unwrap();
        let fourth_word = line_parts.nth(0).unwrap();

        if first_word == "toggle" {
            let mut start_coords = second_word.split(",");

            let start_x_str = start_coords.nth(0).unwrap();
            let start_y_str = start_coords.nth(0).unwrap();

            let start_x = start_x_str.parse::<i32>().unwrap();
            let start_y = start_y_str.parse::<i32>().unwrap();

            let mut end_coords = fourth_word.split(",");

            let end_x_str = end_coords.nth(0).unwrap();
            let end_y_str = end_coords.nth(0).unwrap();

            let end_x = end_x_str.parse::<i32>().unwrap();
            let end_y = end_y_str.parse::<i32>().unwrap();

            toggle(&mut lights, start_x, start_y, end_x, end_y);
        } else if first_word == "turn" {
            let fifth_word = line_parts.nth(0).unwrap();

            let mut start_coords = third_word.split(",");

            let start_x_str = start_coords.nth(0).unwrap();
            let start_y_str = start_coords.nth(0).unwrap();

            let start_x = start_x_str.parse::<i32>().unwrap();
            let start_y = start_y_str.parse::<i32>().unwrap();

            let mut end_coords = fifth_word.split(",");

            let end_x_str = end_coords.nth(0).unwrap();
            let end_y_str = end_coords.nth(0).unwrap();

            let end_x = end_x_str.parse::<i32>().unwrap();
            let end_y = end_y_str.parse::<i32>().unwrap();

            let on_or_off: u8;
            if second_word == "off" {
                on_or_off = 0;
            } else {
                on_or_off = 1;
            }

            turn(&mut lights, start_x, start_y, end_x, end_y, on_or_off);
        }
    }

    let mut sum: u64 = 0;
    for (i, row) in lights.iter().enumerate() {
        for (j, _) in row.iter().enumerate() {
            let i = i as usize;
            let j = j as usize;
            sum += lights[i][j] as u64;
        }
    }

    println!("{sum}");
    Ok(())
}
