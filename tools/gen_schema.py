"""Generate MySQL DDL from MyBatis-Plus entity classes."""
import glob
import os
import re

ENTITY_DIR = os.path.join(os.path.dirname(__file__), "..", "src", "main", "java", "com", "mes", "entity")

TYPE_MAP = {
    "Long": "BIGINT",
    "long": "BIGINT",
    "Integer": "INT",
    "int": "INT",
    "String": "VARCHAR(500)",
    "BigDecimal": "DECIMAL(18,4)",
    "Double": "DOUBLE",
    "double": "DOUBLE",
    "Float": "FLOAT",
    "Boolean": "TINYINT(1)",
    "boolean": "TINYINT(1)",
    "LocalDateTime": "DATETIME",
    "LocalDate": "DATE",
    "LocalTime": "TIME",
    "byte[]": "BLOB",
}


def snake(name):
    return re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()


def parse(path):
    text = open(path, encoding="utf-8").read()
    m = re.search(r'@TableName\("([^"]+)"\)', text)
    if not m:
        return None
    table = m.group(1)
    lines = text.splitlines()
    cols = []
    annot = []
    for line in lines:
        s = line.strip()
        if s.startswith("@TableField") or s.startswith("@TableId"):
            annot.append(s)
            continue
        fm = re.match(r"private\s+([\w<>\[\],\s]+?)\s+(\w+)\s*(?:=.*)?;", s)
        if fm:
            ftype = fm.group(1).strip()
            fname = fm.group(2)
            if ftype not in TYPE_MAP:
                annot = []
                continue
            col = snake(fname)
            for a in annot:
                cm = re.search(r'@TableField\(\s*(?:value\s*=\s*)?"([^"]+)"', a)
                if cm:
                    col = cm.group(1)
            cols.append((col, TYPE_MAP[ftype], fname))
            annot = []
            continue
        if s and not s.startswith("//") and not s.startswith("*") and not s.startswith("/*"):
            annot = []
    if not cols:
        return None
    names = {c[0] for c in cols}
    out = [f"CREATE TABLE IF NOT EXISTS `{table}` ("]
    defs = []
    for col, sqltype, _ in cols:
        if col == "id":
            defs.append("  `id` BIGINT NOT NULL AUTO_INCREMENT")
        else:
            defs.append(f"  `{col}` {sqltype} NULL")
    if "id" in names:
        defs.append("  PRIMARY KEY (`id`)")
    if "is_deleted" in names:
        defs.append("  KEY `idx_is_deleted` (`is_deleted`)")
    out.append(",\n".join(defs))
    out.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;")
    return "\n".join(out)


def main():
    files = sorted(glob.glob(os.path.join(ENTITY_DIR, "*.java")))
    statements = []
    for f in files:
        ddl = parse(f)
        if ddl:
            statements.append(ddl)
    print("\n\n".join(statements))


if __name__ == "__main__":
    main()
