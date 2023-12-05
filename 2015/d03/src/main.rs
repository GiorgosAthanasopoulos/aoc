use std::{
    env,
    fs::File,
    io::{BufRead, BufReader},
};

struct Pair {
    x: i64,
    y: i64,
}

fn main() -> Result<(), &'static str> {
    let args: Vec<String> = env::args().collect();
    if args.len() < 2 {
        return Err("Missing argument: filename");
    }
    let filename = args[1].clone();
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let mut santaX = 0;
    let mut santaY = 0;
    let mut robotX = 0;
    let mut robotY = 0;
    let mut santaMove = true;
    let mut visited: Vec<Pair> = vec![Pair { x: 0, y: 0 }];
    for line in reader.lines() {
        for char in line.unwrap().chars() {
            if santaMove {
                if char == '^' {
                    santaY += 1;
                } else if char == 'v' {
                    santaY -= 1;
                } else if char == '>' {
                    santaX += 1;
                } else if char == '<' {
                    santaX -= 1;
                }
                let pair = Pair { x: santaX, y: santaY };
                if !Visited(&visited, &pair) {
                    visited.push(pair);
                }
            } else {
                if char == '^' {
                    robotY += 1;
                } else if char == 'v' {
                    robotY -= 1;
                } else if char == '>' {
                    robotX += 1;
                } else if char == '<' {
                    robotX -= 1;
                }
                let pair = Pair { x: robotX, y: robotY };
                if !Visited(&visited, &pair) {
                    visited.push(pair);
                }
            }
            santaMove = !santaMove;
        }
    }

    let res = visited.len();
    println!("{res}");
    Ok(())
}

fn Visited(visited: &Vec<Pair>, pair: &Pair) -> bool {
    for _pair in visited {
        if pair.x == _pair.x && pair.y == _pair.y {
            return true
        }
    }
    return false
}
