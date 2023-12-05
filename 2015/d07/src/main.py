import sys

def is_operand(string: str) -> bool:
    return string == "AND" or string == "OR" or string == "LSHIFT" or string == "RSHIFT" or string == "NOT"

def is_wire(a: str) -> bool:
    return a.isalpha()

def parse(line: str) -> tuple[str, str|None, str|None, str]:
    lhs, rhs = line.strip().split("->")
    lhs = lhs.strip()
    rhs = rhs.strip()
    lhs_parts: list[str] = lhs.split(" ")

    a: str = ""
    b: str|None = None
    op: str|None = None
    for token in lhs_parts:
        if is_operand(token):
            op = token.strip()
            lhs_parts.remove(token)
            continue
        if token != "None":
            if a == "":
                a = token.strip()
            else:
                b = token.strip()
    print(line,
          "=>",
          "a:", a,
          "op:", op,
          "b:", b,
          "rhs:", rhs)
    return a, op, b, rhs

def evaluate(a: str, op: str|None, b: str, variables: dict[str, int]) -> int:
    a_is_wire: bool = is_wire(a)
    if a_is_wire and a not in variables.keys():
        return -1
    temp_a: int = variables[a] if a_is_wire else int(a)
    if op is None:
        return temp_a
    if op == "NOT":
        return ~temp_a
    assert(b is not None)
    b_is_wire: bool = is_wire(b)
    if b_is_wire and b not in variables.keys():
        return -1
    temp_b: int = variables[b] if b_is_wire else int(b)
    if op == "AND":
        return temp_a & temp_b
    if op == "OR":
        return temp_a | temp_b
    if op == "LSHIFT":
        return temp_a << temp_b
    if op == "RSHIFT":
        return temp_a >> temp_b
    print(f"unknown operator: {op}")
    return -1

def main() -> None:
    args: list[str] = sys.argv
    if len(args) < 2:
        print("Missing argument: filename")
        exit(1)
    filename: str = args[1]
    variables: dict[str, int] =  {}
    unfinished_operations: list[str] = []
    with open(filename, "r") as f:
        for line in f.readlines():
            line: str = line.replace('\n', '')
            tokens: tuple[str, str|None, str|None, str] = parse(line)
            res: int = evaluate(tokens[0], tokens[1], tokens[3], variables)
            if res == -1:
                unfinished_operations.append(line)
                continue
            variables[tokens[3]] = res
    i: int = 0
    while len(unfinished_operations) > 0:
        operation: str = unfinished_operations[i]
        tokens: tuple[str, str|None, str|None, str] = parse(operation)
        res: int = evaluate(tokens[0], tokens[1], tokens[3], variables)
        i += 1
        if i == len(unfinished_operations):
            i = 0
        if res == -1:
            continue
        variables[tokens[3]] = res
    print(variables["a"])

if __name__ == "__main__":
    main()
